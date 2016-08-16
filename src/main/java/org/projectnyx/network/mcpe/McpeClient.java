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
package org.projectnyx.network.mcpe;

import java.nio.ByteBuffer;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.apache.commons.codec.binary.Hex;

import org.projectnyx.Nyx;
import org.projectnyx.network.Client;
import org.projectnyx.network.Protocol;
import org.projectnyx.network.ext.SessionHandler;
import org.projectnyx.network.ext.udp.UdpSessionHandler;
import org.projectnyx.network.mcpe.raknet.RakNetInterface;
import org.projectnyx.network.mcpe.raknet.RawDataPacket;
import org.projectnyx.network.mcpe.raknet.data.ClientDisconnect;
import org.projectnyx.network.mcpe.raknet.raknet.RakNetPacketManager;

@RequiredArgsConstructor
public class McpeClient extends Client {
    public final static int LOGIN_STEP_WAIT_REQUEST_1 = 0;
    public final static int LOGIN_STEP_WAIT_REQUEST_2 = 1;
    public final static int LOGIN_STEP_WAIT_CONNECT = 2;
    public final static int LOGIN_STEP_WAIT_HANDSHAKE = 3;
    public final static int LOGIN_STEP_IN_GAME = 4;

    @Getter @NonNull private final McpeProtocol protocol;
    @Getter private final UdpSessionHandler session;
    @Getter private RakNetInterface rakNetInterface = new RakNetInterface(this);
    @Getter @Setter private int loginStep = LOGIN_STEP_WAIT_REQUEST_1;

    @Override
    public Protocol getSourceProtocol() {
        return protocol;
    }

    @Override
    public SessionHandler getSessionHandler() {
        return session;
    }

    @Override
    public void handlePacket(ByteBuffer buffer) {
        ReceivedPacket packet = RakNetPacketManager.getInstance().parsePacket(buffer);
        if(packet == null) {
            buffer.position(0);
            Nyx.getInstance().getLog().error(String.format("Cannot handle MCPE packet from client %s - %s",
                    session.getAddress(), Hex.encodeHexString(buffer.array())));
            return;
        }

        rakNetInterface.handlePacket(packet);
    }

    public void handleDataPacket(ByteBuffer buffer) {
        byte pid = buffer.get();
    }

    @Override
    public void close(CloseReason closeReason) {
        if(closeReason != CloseReason.CLIENT_DISCONNECT) {
            RawDataPacket pk = new RawDataPacket();
            pk.reliability = 0;
            pk.buffer = new ClientDisconnect().write().array();
            rakNetInterface.queueDataPacket(pk);
            rakNetInterface.flushDataPacketQueue();
        }

        super.close(closeReason);
    }

    @Override
    public void tick() {
        super.tick();
        rakNetInterface.tick();
    }

    public void unexpectedPacket(ReceivedPacket packet) {
        Nyx.getInstance().getLog().debug(String.format("Unexpected packet from client %s at stage %d - %s",
                session.getAddress(), loginStep, packet.getClass().getSimpleName()));
    }

    public void unexpectedPacket(RawDataPacket packet) {
        Nyx.getInstance().getLog().error(String.format("Unexpected data packet from client %s at stage %d - 0x%x",
                session.getAddress(), loginStep, packet.buffer[0]));
    }
}
