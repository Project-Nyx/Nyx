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
package org.projectnyx.network.ext;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import lombok.Getter;
import lombok.Value;

import org.projectnyx.network.ext.udp.SharedUdpServerSocket;

public abstract class SharedServerSocket {
    @Getter private final Identifier identifier;

    public SharedServerSocket(String type, InetAddress address, int port) {
        this(new Identifier(type, address, port));
    }

    public SharedServerSocket(Identifier identifier) {
        this.identifier = identifier;
        SharedSocketPool.getInstance().addSocket(this);
    }

    public void close() {
        SharedSocketPool.getInstance().removeSocket(this);
    }

    public void tick() {
    }

    @Value
    public static class Identifier {
        String type;
        InetAddress address;
        int port;

        public SharedServerSocket create() {
            switch(type) {
                case SharedUdpServerSocket.TYPE:
                    return new SharedUdpServerSocket(new InetSocketAddress(address, port));
            }
            throw new UnsupportedOperationException("Unknown socket type " + type);
        }
    }
}
