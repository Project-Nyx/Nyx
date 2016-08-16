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
package org.projectnyx.command;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * <p>Parent class for all commands.</p>
 *
 * <p>Each concrete command class should be like singleton (even though without a {@code static getInstance()} method),
 * i.e. a command should only be instantiated when registering.</p>
 */
public abstract class Command {
    /**
     * <p>A unique {@link String} identifier of this command.</p>
     */
    @Getter private final String name;
    /**
     * <p>This command's description, which can be displayed to users.</p>
     */
    @Getter @Setter private UIText description;
    /**
     * <p>This command's usage, which can be displayed to users.</p>
     */
    @Getter @Setter private UIText usage;
    /**
     * <p>Aliases of this command.</p>
     *
     * <p>These are alternative names that have lower priority than names. If a command's name is the same as another
     * command's alias, it will override.</p>
     */
    @Getter private List<String> aliases = new ArrayList<>();

    private CommandIssuer currentIssuer;
    private String currentArgsLine = null;
    private String[] currentArgsArray = null;

    public Command(String name) {
        this(name, (UIText) null, null);
    }

    public Command(String name, String description, String usage) {
        this(name, new StaticPlainText(description), new StaticPlainText(usage));
    }

    public Command(String name, UIText description, UIText usage) {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    /**
     * <p>Trigger this command.</p>
     *
     * @param issuer the {@link CommandIssuer} who issued this command.
     * @param line   the rest of the arguments in the command.
     */
    public final void execute(CommandIssuer issuer, String line) {
        currentIssuer = issuer;
        currentArgsLine = line;
        try {
            run();
        } catch(Throwable e) {
            // TODO
        }
        currentArgsLine = null;
    }

    /**
     * <p>Implementation of the command.</p>
     * <p>Arguments for this execution can be found using these methods:</p>
     *
     * @see #getArgsLine()
     * @see #getArgs()
     * @see #parseArgs(Options)
     */
    protected abstract void run();

    /**
     * <p>The exact line of command without the command name.</p>
     *
     * @return
     */
    public String getArgsLine() {
        throwNullArgs();
        return currentArgsLine;
    }

    /**
     * <p>The {@link #getArgsLine() argument line} line split at whitespaces</p>
     *
     * @return
     */
    public String[] getArgs() {
        throwNullArgs();
        if(currentArgsArray == null) {
            currentArgsArray = currentArgsLine.split(" ");
        }
        return currentArgsArray;
    }

    /**
     * Parse command arguments using {@link org.apache.commons.cli Commons CLI}
     *
     * @param options Options to look for
     * @return the {@link CommandLine} result from {@link org.apache.commons.cli Commons CLI}
     * @throws ParseException if {@link CommandLineParser} encountered problems parsing arguments
     */
    public CommandLine parseArgs(Options options) throws ParseException {
        return parseArgs(options, false);
    }

    /**
     * Parse command arguments using {@link org.apache.commons.cli Commons CLI}
     *
     * @param options         Options to look for
     * @param stopAtNonOption Stop parsing options if there is an unrecognized option
     * @return the {@link CommandLine} result from {@link org.apache.commons.cli Commons CLI}
     * @throws ParseException if {@link CommandLineParser} encountered problems parsing arguments
     */
    public CommandLine parseArgs(Options options, boolean stopAtNonOption) throws ParseException {
        return new DefaultParser().parse(options, getArgs(), stopAtNonOption);
    }

    private void throwNullArgs() {
        if(currentArgsLine == null) {
            throw new IllegalStateException("Attempt to get command args when command is not running");
        }
    }
}
