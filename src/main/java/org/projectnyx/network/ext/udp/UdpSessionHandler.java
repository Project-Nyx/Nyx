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
package org.projectnyx.network.ext.udp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.Getter;

import org.projectnyx.Nyx;
import org.projectnyx.network.Client;
import org.projectnyx.network.NetworkManager;
import org.projectnyx.network.Protocol;
import org.projectnyx.network.ext.PacketWithAddress;
import org.projectnyx.network.ext.SessionHandler;

public class UdpSessionHandler extends SessionHandler {
    @Getter private UdpInitStack initStack = new UdpInitStack();
    @Getter private Protocol protocol;
    @Getter private Client client;

    public UdpSessionHandler(InetSocketAddress address, SharedUdpServerSocket socket) {
        super(address, socket);
    }

    @Override
    public void onReceive(PacketWithAddress pk) {
        if(protocol == null) {
            Nyx.getLog().debug(String.format("Handling packet from %s", pk.getAddress()));
            initStack.add(pk.getBuffer());
            Client client = NetworkManager.getInstance().adoptSession(this);
            if(client != null) {
                protocol = client.getSourceProtocol();
                this.client = client;
                initStack = null;
                Nyx.getLog().debug(String.format("Client %s is adopted by %s", getAddress(), protocol.getName()));
            }
        } else {
            Nyx.getLog().debug(String.format("Handling packet from %s", pk.getAddress()));
            client.handlePacket(pk.getBuffer());
        }
    }

    @Override
    public void send(ByteBuffer buffer) {
        ((SharedUdpServerSocket) getSocket()).getThread().pushSend(new PacketWithAddress(buffer, getAddress(), buffer.position() != 0));
    }

    @Override
    public void close() {
        ((SharedUdpServerSocket) getSocket()).onSessionClosed(this);
    }

    @Override
    public void tick() {
        super.tick();
        if(client != null) {
            client.tick();
        }
    }

    @Override
    public boolean isOrphan() {
        return client == null;
    }
}
