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

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.projectnyx.network.ext.PacketWithAddress;

@RequiredArgsConstructor
public class UdpBufferThread extends Thread {
    private final SharedUdpServerSocket owner;
    private final InetSocketAddress address;

    private DatagramChannel channel;
    private DatagramSocket socket;

    private final List<PacketWithAddress> receive = new LinkedList<>();
    private final List<PacketWithAddress> send = new LinkedList<>();

    private boolean running = true;

    @Override
    public void run() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(true);
            channel.socket().bind(address);
            socket = channel.socket();
            socket.setReceiveBufferSize(8 << 20);
            socket.setSendBufferSize(8 << 20);
            while(running) {
                execLoop();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void execLoop() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8 << 20);
        InetSocketAddress address = (InetSocketAddress) channel.receive(buffer);
        if(address != null) {
            pushReceive(new PacketWithAddress(buffer, address));
        }
        PacketWithAddress pk;
        while((pk = shiftSend()) != null) {
            channel.send(pk.getBuffer(), pk.getAddress());
        }
    }

    private void pushReceive(PacketWithAddress packet) {
        synchronized(receive) {
            receive.add(packet);
        }
    }

    public PacketWithAddress shiftReceive() {
        PacketWithAddress pk = null;
        synchronized(receive) {
            if(receive.size() > 0) {
                pk = receive.remove(0);
            }
        }
        return pk;
    }

    public void pushSend(PacketWithAddress packet) {
        synchronized(receive) {
            send.add(packet);
        }
    }

    private PacketWithAddress shiftSend() {
        PacketWithAddress pk = null;
        synchronized(send) {
            if(send.size() > 0) {
                pk = send.remove(0);
            }
        }
        return pk;
    }
}
