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
import lombok.RequiredArgsConstructor;

import org.projectnyx.network.mcpe.ReceivedPacket;
import org.projectnyx.network.mcpe.raknet.RawDataPacket;

@RequiredArgsConstructor
public class ReceivedDataPacketGroup extends ReceivedPacket {
    private final byte pid;

    public int seqNumber;
    public List<RawDataPacket> packets = new ArrayList<>();

    @Override
    public byte pid() {
        return pid;
    }

    @Override
    protected void decode() {
        seqNumber = getLTriad();
        while(getBuffer().hasRemaining()) {
            RawDataPacket packet = RawDataPacket.read(this);
            if(packet.buffer.length == 0) {
                break;
            }
            packets.add(packet);
        }
    }
}
