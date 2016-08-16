/*
 * Nyx - Server software for Minecraft: PE and more
 * Copyright © boredphoton 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projectnyx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import lombok.Builder;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.projectnyx.command.CommandManager;
import org.projectnyx.command.ConsoleReader;
import org.projectnyx.config.ConfigParser;
import org.projectnyx.module.ModuleManager;
import org.projectnyx.network.NetworkManager;
import org.projectnyx.player.PlayerManager;
import org.projectnyx.properties.NyxConfig;
import org.projectnyx.ticker.Ticker;
import org.projectnyx.util.Util;

/**
 * <p>Singleton central object of the whole server.</p>
 */
public class Nyx {
    /**
     * The singleton instance of Nyx
     */
    @Getter private static Nyx instance = null;

    /**
     * <p>The directory where JAR module files are loaded</p>
     * <p>Can be specified from command line using the <code>-m</code> (<code>--module-path</code>) option.</p>
     */
    @Getter private final File modulePath;
    /**
     * <p>The parent directory of all simple loaded and saved data of this server instance.</p>
     * <p>Can be specified from command line using the {@code -d} ({@code --data-path}) option.</p>
     * <p>Developers who clone this project should set this path to an outside directory or one of the files listed in
     * {@code .gitignore}. The {@code /data/} entry was designated for this use.</p>
     */
    @Getter private final File dataPath;
    /**
     * <p>The {@link Logger log4j2 logger} for this server</p>
     */
    @Getter private final Logger log = LogManager.getLogger(getClass());
    /**
     * <p>The loaded {@code nyx.xml} config file</p>
     */
    @Getter private final NyxConfig config;
    /**
     * <p>An internal list of module managers used to accept modules</p>
     */
    @Getter private final Map<String, ModuleManager<?>> moduleManagers = new HashMap<>();
    /**
     * <p>{@link Ticker} is the server's core pacemaker. Manages the main loop that keeps the server on.</p>
     */
    @Getter private final Ticker ticker;
    /**
     * <p>The {@link NetworkManager} for this server.</p>
     */
    @Getter private final NetworkManager network;
    /**
     * <p>The {@link PlayerManager} for this server.</p>
     */
    @Getter private final PlayerManager players;
    /**
     * <p>The {@link CommandManager} for this server.</p>
     */
    @Getter private final CommandManager commandManager;
    /**
     * <p>A thread in charge of reading command input from console ({@link System#in}).</p>
     */
    @Getter private final ConsoleReader console;
    /**
     * <p>A list of tasks to run <em>after</em> the last tick of the server is completed.</p>
     */
    @Getter private final List<Runnable> shutdownTasks = new ArrayList<>();

    @Builder
    private Nyx(File modulePath, File dataPath, long serverStartTime) {
        if(instance != null) {
            throw new IllegalStateException("An instance of Nyx already exists");
        }
        instance = this;

        this.modulePath = modulePath;
        this.dataPath = dataPath;
        log.info("Loading configuration...");
        config = loadConfig();

        network = new NetworkManager();
        players = new PlayerManager();
        ticker = new Ticker();
        commandManager = new CommandManager();
        console = new ConsoleReader();

        log.info("Loading modules...");
        loadBuiltinModules();
        loadJarModules();

        console.start();

        log.info(String.format("Server started (%s s)", (System.currentTimeMillis() - serverStartTime) / 1000d));
        ticker.startTicking();
    }

    @SneakyThrows({IOException.class})
    private NyxConfig loadConfig() {
        File configFile = new File(dataPath, "nyx.xml");
        if(!configFile.isFile()) {
            IOUtils.copy(getClass().getClassLoader().getResourceAsStream("nyx.xml"), new FileOutputStream(configFile));
        }

        return (NyxConfig) new ConfigParser(configFile).parse();
    }

    @SneakyThrows({IOException.class})
    private void loadBuiltinModules() {
        URL resource = getClass().getClassLoader().getResource("mods.list");
        assert resource != null;
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()));
        reader.lines().forEach(line -> {
            line = line.trim();
            String[] args = line.split(":", 3);
            if(line.charAt(0) == '#' || args.length != 3) {
                return;
            }
            String moduleType = args[0].trim();
            if(moduleManagers.containsKey(moduleType)) {
                try {
                    log.info(String.format("Loading builtin %s module: %s", moduleType, args[1]));
                    moduleManagers.get(moduleType).loadForClass(args[1].trim(), args[2].trim());
                } catch(Exception e) {
                    log.error("Cannot load builtin module", e);
                }
            } else {
                log.error("Unknown module type: " + moduleType);
            }
        });
    }

    private void loadJarModules() {
        File[] files = modulePath.listFiles((dir, name) -> name.toLowerCase(Locale.ENGLISH).endsWith(".jar"));
        assert files != null;
        for(File file : files) {
            Properties props;
            try {
                props = Util.loadJar(file);
            } catch(IOException e) {
                e.printStackTrace();
                continue;
            }
            String moduleType = props.getProperty("type");
            if(moduleManagers.containsKey(moduleType)) {
                try {
                    log.info(String.format("Loading %s module from jar: %s", moduleType, props.getProperty("name")));
                    moduleManagers.get(moduleType).loadForClass(props.getProperty("name"), props.getProperty("mainClass"));
                } catch(Exception e) {
                    log.error("Cannot load jar module", e);
                }
            } else {
                log.error("Jar module " + file.getName() + " declares an unknown module type: " + moduleType);
            }
        }
    }

    /**
     * <p>Adds a {@link Runnable} task that will be run <em>after</em> the last server tick is completed.</p>
     *
     * @param runnable the task to run
     */
    public void addShutdownTask(Runnable runnable) {
        shutdownTasks.add(runnable);
    }

    /**
     * <p>Causes the current server tick to be the last tick that runs, followed by running the {@link
     * #shutdownTasks}.</p>
     *
     * <p>This method will not block until the last server tick is completed. Actually, this method should only be
     * triggered from the main thread, i.e. from a server tick.</p>
     */
    public void shutdown() {
        ticker.stopTicking();
    }
}
