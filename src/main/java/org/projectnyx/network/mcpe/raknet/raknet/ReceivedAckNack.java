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
package org.projectnyx.network.mcpe.raknet.raknet;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

import org.projectnyx.network.mcpe.ReceivedPacket;

@ToString
public abstract class ReceivedAckNack extends ReceivedPacket {
    @Getter private List<Integer> seqNumbers;

    @Override
    protected void decode() {
        seqNumbers = new ArrayList<>();
        short count = getShort();
        for(int i = 0; i < count && buffer.hasRemaining(); i++) {
            boolean single = getByte() != 0;
            if(single) {
                seqNumbers.add(getLTriad());
            } else {
                int start = getLTriad();
                int end = Math.min(start + 512, getLTriad());

                for(int j = start; j <= end; j++) {
                    seqNumbers.add(j);
                }
            }
        }
    }
}
