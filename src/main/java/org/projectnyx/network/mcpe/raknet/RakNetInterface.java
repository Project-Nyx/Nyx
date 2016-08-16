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
package org.projectnyx.network.mcpe.raknet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.commons.codec.binary.Hex;

import org.projectnyx.Nyx;
import org.projectnyx.network.Client;
import org.projectnyx.network.mcpe.McpeClient;
import org.projectnyx.network.mcpe.ReceivedPacket;
import org.projectnyx.network.mcpe.SentPacket;
import org.projectnyx.network.mcpe.raknet.data.ClientConnect;
import org.projectnyx.network.mcpe.raknet.data.ClientHandshake;
import org.projectnyx.network.mcpe.raknet.data.Ping;
import org.projectnyx.network.mcpe.raknet.data.Pong;
import org.projectnyx.network.mcpe.raknet.data.ServerHandshake;
import org.projectnyx.network.mcpe.raknet.raknet.*;

import static org.projectnyx.network.mcpe.McpeClient.*;
import static org.projectnyx.network.mcpe.raknet.RakNetConsts.*;

@RequiredArgsConstructor
public class RakNetInterface {
    @Getter private final McpeClient client;
    private final List<RawDataPacket> sendQueue = new ArrayList<>();
    private int nextSeqNumber = 0;
    private final Map<Integer, SentDataPacketGroup> nackRecovery = new HashMap<>();
    private final List<SentDataPacketGroup> nackReplyQueue = new LinkedList<>();
    private final List<Integer> ackQueue = new ArrayList<>();
    private final List<Integer> nackQueue = new ArrayList<>(); // TODO implement NACK
    private Map<Long, Ping> pingMap = new HashMap<>();
    private long lastPing = 0;

    public void handlePacket(ReceivedPacket packet) {
//        Nyx.getInstance().getLog().debug(String.format("Received %s from %s", packet.getClass().getSimpleName(), client.getAddress()));

        if(packet instanceof ReceivedDataPacketGroup) {
            ReceivedDataPacketGroup group = (ReceivedDataPacketGroup) packet;
            group.packets.forEach(this::handleDataPacket);
            ackQueue.add(group.seqNumber);
            return;
        }
        if(packet instanceof ReceivedAck) {
            ((ReceivedAck) packet).getSeqNumbers().forEach(nackRecovery::remove);
            return;
        }
        if(packet instanceof ReceivedNack) {
            for(Integer seqNumber : ((ReceivedNack) packet).getSeqNumbers()) {
                SentDataPacketGroup resend = nackRecovery.remove(seqNumber);
                if(resend == null) {
                    Nyx.getInstance().getLog().debug(String.format("Client %s sent NACK %s for seqNumber %d. Buffer: %s",
                            client.getAddress(), packet, seqNumber, Hex.encodeHexString(packet.getBuffer().array())));
                    return;
                }
                resend.seqNumber = getNextSeqNumber();
                queueNackReply(resend);
            }
            return;
        }

        if(client.getLoginStep() == LOGIN_STEP_WAIT_REQUEST_1) {
            if(!(packet instanceof OpenConnectionRequest1)) {
                client.unexpectedPacket(packet);
                return;
            }
            OpenConnectionRequest1 request = (OpenConnectionRequest1) packet;
            OpenConnectionReply1 reply = new OpenConnectionReply1();
            reply.serverId = client.getProtocol().getServerId();
            reply.mtuSize = (short) request.getMtuSize();
            sendRakNetPacket(reply);
            client.setLoginStep(LOGIN_STEP_WAIT_REQUEST_2);
        } else if(client.getLoginStep() == LOGIN_STEP_WAIT_REQUEST_2) {
            if(!(packet instanceof OpenConnectionRequest2)) {
                client.unexpectedPacket(packet);
                return;
            }
            OpenConnectionRequest2 request = (OpenConnectionRequest2) packet;
            OpenConnectionReply2 reply = new OpenConnectionReply2();
            reply.serverId = client.getProtocol().getServerId();
            reply.address = client.getSession().getAddress();
            reply.mtuSize = request.getMtuSize();
            sendRakNetPacket(reply);
            client.setLoginStep(LOGIN_STEP_WAIT_CONNECT);
        } else {
            client.unexpectedPacket(packet);
        }
    }

    private void handleDataPacket(RawDataPacket packet) {
        ByteBuffer buffer = ByteBuffer.wrap(packet.buffer).order(ByteOrder.BIG_ENDIAN);
        byte pid = buffer.get();

        if(pid == DATA_PING) {
            long pingId = buffer.getLong();
            Pong pong = new Pong();
            pong.pingId = pingId;
            RawDataPacket pk = new RawDataPacket();
            pk.reliability = 0;
            pk.buffer = pong.write().array();
            queueDataPacket(pk);
            return;
        }
        if(pid == DATA_PONG) {
            long pingId = buffer.getLong();
            Ping ping = pingMap.remove(pingId);
            if(ping == null) {
                Nyx.getInstance().getLog().error(String.format("Client from %s sent PONG with unknown ping ID %d",
                        client.getAddress(), pingId));
                return;
            }
            client.getPingStats().addDatum(ping.sendTimeNano, true);
            return;
        }
        if(pid == DATA_CLIENT_DISCONNECT) {
            client.close(Client.CloseReason.CLIENT_DISCONNECT);
        }

        if(client.getLoginStep() == LOGIN_STEP_WAIT_CONNECT) {
            if(pid != DATA_CLIENT_CONENCT) {
                client.unexpectedPacket(packet);
                return;
            }
            ClientConnect request = new ClientConnect(buffer);
            ServerHandshake response = new ServerHandshake();
            response.address = client.getSession().getAddress();
            response.ping = request.pingTime;
            response.pong = request.pingTime + 1000;
            RawDataPacket pk = new RawDataPacket();
            pk.reliability = 0;
            pk.buffer = response.write().array();
            queueDataPacket(pk);
            flushDataPacketQueue();
            client.setLoginStep(LOGIN_STEP_WAIT_HANDSHAKE);
        } else if(client.getLoginStep() == LOGIN_STEP_WAIT_HANDSHAKE) {
            if(pid != DATA_CLIENT_HANDSHAKE) {
                client.unexpectedPacket(packet);
                return;
            }
            ClientHandshake handshake = new ClientHandshake(buffer);
            // TODO log referrer?
            // TODO check port?
            client.setLoginStep(LOGIN_STEP_IN_GAME);
            Nyx.getInstance().getLog().debug(String.format("Client %s is now logging in!", client.getAddress()));
        } else if(client.getLoginStep() != LOGIN_STEP_IN_GAME) {
            client.unexpectedPacket(packet);
        } else {
            if(pid != RakNetConsts.DATA_MCPE) {
                client.unexpectedPacket(packet);
                return;
            }

            client.handleDataPacket(buffer);
        }
    }

    public void sendRakNetPacket(SentPacket packet) {
        client.getSession().send(packet.write());
    }

    public void queueDataPacket(RawDataPacket packet) {
        Nyx.getInstance().getLog().debug(String.format("Sending data packet 0x%x to %s: %s", packet.buffer[0], client.getAddress(), Hex.encodeHexString(packet.buffer)));
        sendQueue.add(packet);
    }

    public void flushDataPacketQueue() {
        SentDataPacketGroup group = new SentDataPacketGroup((byte) 0x84); // FIXME why?
        group.seqNumber = getNextSeqNumber();
        group.lastSendNanos = System.nanoTime();
        group.getPackets().addAll(sendQueue);
        sendRakNetPacket(group);
        nackRecovery.put(group.seqNumber, group);
        sendQueue.clear();
    }

    public void sendDataPacketImmediate(RawDataPacket packet) {
        SentDataPacketGroup group = new SentDataPacketGroup((byte) 0); // FIXME why?
        group.seqNumber = getNextSeqNumber();
        group.lastSendNanos = System.nanoTime();
        group.getPackets().add(packet);
        sendRakNetPacket(group);
        nackRecovery.put(group.seqNumber, group);
    }

    public void queueNackReply(SentDataPacketGroup group) {
        nackReplyQueue.add(group);
    }

    public void flushNackReplyQueue() {
        Iterator<SentDataPacketGroup> iterator = nackReplyQueue.iterator();
        for(int i = 0; i < 16 && iterator.hasNext(); i++) {
            sendRakNetPacket(iterator.next());
            iterator.remove();
        }
        // TODO manage reliability
    }

    public void flushAckQueue() {
        if(ackQueue.isEmpty()) {
            return;
        }
        SentAckNack ack = new SentAckNack(SentAckNack.ACK);
        ack.getSeqNumbers().addAll(ackQueue);
        sendRakNetPacket(ack);
        ackQueue.clear();
    }

    public void flushNackQueue() {
        if(nackQueue.isEmpty()) {
            return;
        }
        SentAckNack nack = new SentAckNack(SentAckNack.NACK);
        nack.getSeqNumbers().addAll(nackQueue);
        sendRakNetPacket(nack);
        nackQueue.clear();
    }

    public void tick() {
        flushDataPacketQueue();
        flushNackReplyQueue();
        flushAckQueue();
        flushNackQueue();
        pingMap = pingMap.entrySet().stream().filter(this::filterPingMapEntry).collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        if(lastPing - System.currentTimeMillis() > 2000) {
            firePing();
        }
    }

    private boolean filterPingMapEntry(Entry<Long, Ping> entry) {
        if(System.nanoTime() - entry.getValue().sendTimeNano > 10e+9) {
            client.getPingStats().addDatum(entry.getValue().sendTimeNano, false);
            return false;
        }
        return true;
    }

    public void firePing() {
        long pingId = new Random().nextLong();
        Ping ping = new Ping();
        ping.pingId = pingId;
        RawDataPacket pk = new RawDataPacket();
        pk.reliability = 0;
        pk.buffer = ping.write().array();
        queueDataPacket(pk);
        pingMap.put(pingId, ping);
        flushDataPacketQueue(); // FIXME should do this?
    }

    public int getNextSeqNumber() {
        return nextSeqNumber++;
    }
}
