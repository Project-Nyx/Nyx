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
package org.projectnyx.network.mcpe.mcpe;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

public class McpeDataPacketManager {
    @Getter private static McpeDataPacketManager instance = new McpeDataPacketManager();

    @Getter private Map<Byte, DataPacket> packets = new HashMap<>();

    public McpeDataPacketManager(){
        registerPackets();
    }

    private void registerPackets() {
        registerPacket(new LoginPacket());
    }

    public void registerPacket(@NonNull DataPacket instance) {
        packets.put(instance.getPacketId(), instance);
    }

    public DataPacket get(ByteBuffer buffer) {
        return get(buffer.get(), buffer);
    }

    public DataPacket get(byte pid, ByteBuffer rest){
        if(packets.containsKey(pid)) {
            return packets.get(pid).clone().setBuffer(rest).parse();
        }
        return null;
    }
}
