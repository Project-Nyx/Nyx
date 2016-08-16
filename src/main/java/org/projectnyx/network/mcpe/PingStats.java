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
package org.projectnyx.network.mcpe;

import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static java.lang.Double.POSITIVE_INFINITY;

public class PingStats {
    @Getter private List<Datum> data = new LinkedList<>();

    public void addDatum(long sendTime, boolean success) {
        Datum datum = success ? new Datum(sendTime, (System.nanoTime() - sendTime) * 1e-9d) : new Datum(sendTime);
        while(data.size() > 20) {
            data.remove(0);
        }
        data.add(datum);
    }

    public double getAverageSuccessfulPing() {
        int count = 0;
        double total = 0d;
        for(Datum datum : data) {
            if(datum.isSuccessful()) {
                count++;
                total += datum.getLatencySecs();
            }
        }
        return total / count;
    }

    public double getPacketLossRatio() {
        return data.stream().filter(datum -> !datum.isSuccessful()).count() / (double) data.size();
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Datum {
        private final long sendTime;
        private double latencySecs = POSITIVE_INFINITY;

        public boolean isSuccessful() {
            return latencySecs != POSITIVE_INFINITY;
        }
    }
}
