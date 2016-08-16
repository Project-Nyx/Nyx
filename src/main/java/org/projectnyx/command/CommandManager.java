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

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

import org.projectnyx.command.impl.StopCommand;

public class CommandManager {
    @Getter private Map<String, Command> commands = new HashMap<>();

    public CommandManager() {
        registerDefaults();
    }

    private void registerDefaults() {
        registerCommand(new StopCommand());
    }

    /**
     * Register a command.
     *
     * @param command
     */
    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
        for(String alias : command.getAliases()) {
            commands.putIfAbsent(alias, command);
        }
    }

    /**
     * Unregister a command.
     *
     * @param command
     */
    public void unregisterCommand(Command command) {
        while(commands.values().remove(command)) {
        }
    }

    /**
     * Handle a line of command sent from a command issuer
     *
     * @param issuer
     * @param commandLine
     */
    public void handleCommand(CommandIssuer issuer, String commandLine) {
        int index = commandLine.indexOf(' ');
        String commandName;
        if(index != -1) {
            commandName = commandLine.substring(0, index);
            commandLine = commandLine.substring(index + 1);
        } else {
            commandName = commandLine;
            commandLine = "";
        }
        if(commands.containsKey(commandName)) {
            commands.get(commandName).execute(issuer, commandLine);
        } else {
            issuer.sendText("Command doesn't exist!");
        }
    }
}
