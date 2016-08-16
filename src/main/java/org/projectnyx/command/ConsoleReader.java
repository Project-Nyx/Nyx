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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import lombok.SneakyThrows;

import org.projectnyx.Nyx;
import org.projectnyx.ticker.Task;
import org.projectnyx.ticker.Ticker;

/**
 * This {@link Thread} listens for the console input from {@link System#in}.
 */
public class ConsoleReader extends Thread implements CommandIssuer {
    private BufferedReader reader;
    private boolean running = true;

    private final List<String> outBuffer = new LinkedList<>();

    @Override
    public synchronized void start() {
        super.start();

        Ticker.getInstance().addTask(1, Task.RUN_INFINITY, this::mainThreadTick);
    }

    private void mainThreadTick() {
        String next;
        while((next = getNext()) != null) {
            Nyx.getInstance().getCommandManager().handleCommand(this, next.trim());
        }
    }

    @Override
    @SneakyThrows({IOException.class})
    public void run() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        while(running) {
            String line = reader.readLine().trim();
            if(line.length() > 0) {
                synchronized(outBuffer) {
                    outBuffer.add(line);
                }
            }
        }
    }

    /**
     * Returns the next command line to be dispatched.
     *
     * @return the next command line to run, or {@code null} if there are no commands to run.
     */
    public String getNext() {
        try {
            synchronized(outBuffer) {
                return outBuffer.remove(0);
            }
        } catch(IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void sendText(UIText text) {
        Nyx.getInstance().getLog().info(text.toANSI(this));
    }
}
