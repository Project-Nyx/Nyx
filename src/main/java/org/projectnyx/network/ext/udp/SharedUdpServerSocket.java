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
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;

import org.projectnyx.Nyx;
import org.projectnyx.network.ext.PacketWithAddress;
import org.projectnyx.network.ext.SharedServerSocket;
import org.projectnyx.ticker.Task;

public class SharedUdpServerSocket extends SharedServerSocket {
    public final static String TYPE = "UDP";
    @Getter private final UdpBufferThread thread;

    private Task task;
    private Map<InetSocketAddress, UdpSessionHandler> sessions = new HashMap<>();
    private List<UdpSessionHandler> sessionsToClose = new LinkedList<>();

    public SharedUdpServerSocket(InetSocketAddress address) {
        super(TYPE, address.getAddress(), address.getPort());
        thread = new UdpBufferThread(this, address);
        thread.start();
        Nyx.getLog().info(String.format("Started UDP server socket listening on %s", address.toString()));
    }

    @Override
    public void tick() {
        super.tick();

        PacketWithAddress pk;
        while((pk = thread.shiftReceive()) != null) {
            UdpSessionHandler session = getSessionOrCreate(pk.getAddress());
            Nyx.getLog().debug(String.format("Received packet from %s", pk.getAddress()));
            session.onReceive(pk);
            Nyx.getLog().debug(String.format("Handled packet from %s", pk.getAddress()));
        }

        sessions.values().forEach(UdpSessionHandler::tick);
        for(UdpSessionHandler handler : sessionsToClose) {
            sessions.values().remove(handler);
        }
        sessionsToClose.clear();
    }

    public UdpSessionHandler getSession(InetSocketAddress address) {
        return sessions.get(address);
    }

    public UdpSessionHandler getSessionOrCreate(InetSocketAddress address) {
        if(!sessions.containsKey(address)) {
            sessions.put(address, new UdpSessionHandler(address, this));
        }
        return sessions.get(address);
    }

    void addSession(UdpSessionHandler session) {
        sessions.put(session.getAddress(), session);
    }

    public void onSessionClosed(UdpSessionHandler handler) {
        if(handler.getSocket() != this) {
            throw new IllegalArgumentException();
        }
        sessionsToClose.add(handler);
    }

    public Collection<UdpSessionHandler> getSessions() {
        return sessions.values();
    }

    @Override
    public void close() {
        super.close();
    }
}
