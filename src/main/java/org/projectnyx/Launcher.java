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

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fusesource.jansi.AnsiConsole;

/**
 * Main class
 */
public final class Launcher {
    /**
     * Don't instantiate this entry class.
     */
    private Launcher() {
        assert false;
    }

    /**
     * Entry method from command line
     *
     * @param args Arguments from command line
     * @throws ParseException Exit directly if there are command line problems. This is dependent on the terminal that
     *                        started this program, so it is applicable to return the error to terminal directly.
     */
    public static void main(String[] args) throws ParseException {
        long serverStartTime = System.currentTimeMillis();
        Options options = new Options()
                .addOption("m", "module-path", true, "Path to the directory to load module files in")
                .addOption("d", "data-path", true, "Path to the directory to store data in");
        CommandLine line = new DefaultParser().parse(options, args);
        AnsiConsole.systemInstall();

        File modules = new File(line.getOptionValue("m", "modules")).getAbsoluteFile();
        modules.mkdirs();
        File data = new File(line.getOptionValue("d", ".")).getAbsoluteFile();
        data.mkdirs();
        Nyx.builder()
                .modulePath(modules)
                .dataPath(data)
                .serverStartTime(serverStartTime)
                .build();
    }
}
