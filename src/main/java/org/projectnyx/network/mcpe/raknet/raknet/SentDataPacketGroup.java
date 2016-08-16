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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.projectnyx.network.mcpe.SentPacket;
import org.projectnyx.network.mcpe.raknet.RawDataPacket;

@RequiredArgsConstructor
public class SentDataPacketGroup extends SentPacket implements Cloneable {
    private final byte id;
    public int seqNumber;
    public long lastSendNanos;
    @Getter private final List<RawDataPacket> packets = new ArrayList<>();
    @Getter(lazy = true) private final byte[] packetData = writePackets();

    private byte[] writePackets() {
        int length = 0;
        for(RawDataPacket packet : packets) {
            length += packet.binaryLength();
        }
        byte[] buffer = new byte[length];
        int offset = 0;
        for(RawDataPacket packet : packets) {
            byte[] add = packet.write();
            System.arraycopy(add, 0, buffer, offset, add.length);
            offset += add.length;
        }
        return buffer;
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    protected int length() {
        return 3 + getPacketData().length;
    }

    @Override
    protected void encode() {
        putLTriad(seqNumber);
        putBytes(getPacketData());
    }

    @SneakyThrows(CloneNotSupportedException.class)
    public SentDataPacketGroup getClone() {
        return (SentDataPacketGroup) super.clone();
    }
}
