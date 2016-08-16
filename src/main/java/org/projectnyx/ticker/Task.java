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

import lombok.Getter;
import lombok.Setter;

public class Task {
    public final static int RUN_INFINITY = -2;
    public final static int RUN_ONCE = 1;

    @Getter private final int interval;
    private int tickCycler;
    @Getter private int runsLeft;
    @Getter @Setter private Runnable runnable;

    /**
     * Superclass constructor for subclasses.
     *
     * @param ticks    number of ticks before the task runs the first time, as well as the repeating interval of the
     *                 task cycle, in ticks.
     * @param runCount number of times to run the task. Pass {@link #RUN_INFINITY} to run an indefinite number of
     *                 times.
     */
    protected Task(int ticks, int runCount) {
        this(ticks, runCount, null);
    }

    /**
     * Creates a Task that stores the timing parameters for a task
     *
     * @param repeatInterval number of ticks before the task runs the first time, as well as the repeating interval of
     *                       the task cycle, in ticks.
     * @param runCount       number of times to run the task. Pass {@link #RUN_INFINITY} to run an indefinite number of
     *                       times.
     * @param runnable       the task to run
     */
    public Task(int repeatInterval, int runCount, Runnable runnable) {
        this(repeatInterval, repeatInterval, runCount, runnable);
    }

    /**
     * Creates a Task that stores the timing parameters for a task
     *
     * @param delayTicks     number of ticks before the the first time this task is run. Start counting from the current
     *                       tick (if {@link Ticker} has started ticking). Pass 0 or 1 to run the task in the current
     *                       tick (if {@link Ticker} has started ticking).
     * @param repeatInterval the repeating interval of the task cycle, in ticks.
     * @param runCount       number of times to run the task. Pass {@link #RUN_INFINITY} to run an indefinite number of
     *                       times.
     * @param runnable       the task to run, or {@code null} if this class is overriden
     */
    public Task(int delayTicks, int repeatInterval, int runCount, Runnable runnable) {
        tickCycler = delayTicks;
        interval = repeatInterval;
        runsLeft = runCount;
        this.runnable = runnable;
    }

    public void tick() {
        tickCycler--;
        if(tickCycler <= 0) {
            run();
            if(runsLeft != RUN_INFINITY) {
                runsLeft--;
                if(runsLeft == 0) {
                    removeTask();
                    return;
                }
            }
            tickCycler = interval;
        }
    }

    protected void run() {
        if(runnable != null) {
            runnable.run();
        }
    }

    public void removeTask() {
        runsLeft = 0;
        Ticker.getInstance().removeTask(this);
    }

    public void addSelf() {
        Ticker.getInstance().addTask(this);
    }
}
