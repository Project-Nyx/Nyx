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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.SneakyThrows;

import org.projectnyx.network.mcpe.ReceivedPacket;
import org.projectnyx.network.mcpe.raknet.RakNetConsts;

public class RakNetPacketManager {
    @Getter private static RakNetPacketManager instance;

    static {
        instance = new RakNetPacketManager();
        instance.registerPackets();
    }

    private Map<Byte, ReceivedPacket> packetTypes = new HashMap<>();

    @SneakyThrows({IllegalAccessException.class, InstantiationException.class})
    private void registerPackets() {
        registerPacket(OpenConnectionRequest1.class);
        registerPacket(OpenConnectionRequest2.class);
        for(byte pid = RakNetConsts.DATA_PACKET_MIN; pid <= RakNetConsts.DATA_PACKET_MAX; pid++) {
            registerPacket(pid, new ReceivedDataPacketGroup(pid));
        }
        registerPacket(ReceivedAck.class);
        registerPacket(ReceivedNack.class);
    }

    public void registerPacket(Class<? extends ReceivedPacket> clazz) throws IllegalAccessException, InstantiationException {
        ReceivedPacket pk = clazz.newInstance();
        registerPacket(pk.getId(), pk);
    }

    public void registerPacket(byte pid, ReceivedPacket packet) {
        packetTypes.put(pid, packet);
    }

    public ReceivedPacket parsePacket(ByteBuffer buffer) {
        byte pid = buffer.get();
        if(packetTypes.containsKey(pid)) {
            ReceivedPacket packet = packetTypes.get(pid).getClone();
            packet.parse(buffer);
            return packet;
        }
        return null;
    }
}
