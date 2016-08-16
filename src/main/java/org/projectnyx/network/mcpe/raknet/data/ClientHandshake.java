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
package org.projectnyx.network.mcpe.raknet.data;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.projectnyx.network.ByteBufferReader;

public class ClientHandshake extends ByteBufferReader {
    public InetSocketAddress address;
    public InetSocketAddress[] systemAddresses = new InetSocketAddress[10];
    public long sendPing;
    public long sendPong;

    public ClientHandshake(ByteBuffer data) {
        buffer = data;
        address = getAddress();
        for(int i = 0; i < 10; i++) {
            systemAddresses[i] = getAddress();
        }
        sendPing = getLong();
        sendPong = getLong();
    }
}
