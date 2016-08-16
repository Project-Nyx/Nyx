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
package org.projectnyx.ticker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

import org.projectnyx.Nyx;

public class Ticker {
    public final static long TICK_DURATION_NANOS = (long) 50_000_000;

    @Getter private static Ticker instance;

    private boolean running = true;
    private int tickCount = 0; // we can last for 3 years and 4 months using int

    private List<Task> taskList = new LinkedList<>();
    private Iterator<Task> currentTaskIterator = null;
    private Task currentIteratingTask;

    public Ticker() {
        instance = this;
    }

    public void startTicking() {
        long next = System.nanoTime();
        while(running) {
            doTick();

            next += TICK_DURATION_NANOS;
            try {
                TimeUnit.NANOSECONDS.sleep(next - System.nanoTime());
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        Nyx.getInstance().getShutdownTasks().forEach(Runnable::run);
    }

    private void doTick() {
        currentTaskIterator = taskList.iterator();
        while(currentTaskIterator.hasNext()) {
            currentIteratingTask = currentTaskIterator.next();
            currentIteratingTask.run();
        }
        currentTaskIterator = null;
        currentIteratingTask = null;

        tickCount++;
    }

    public void addTask(int ticks, int runCount, Runnable runnable) {
        addTask(new Task(ticks, runCount, runnable));
    }

    public void addTask(Task task) {
        Nyx.getInstance().throwMainThread();

        taskList.add(task);
    }

    public void removeTask(Task task) {
        if(task == currentIteratingTask) {
            currentTaskIterator.remove();
        } else {
            if(currentTaskIterator != null) {
                taskList = new LinkedList<>(taskList);
            }
            taskList.remove(task);
        }
    }

    public void stopTicking() {
        running = false;
    }
}
