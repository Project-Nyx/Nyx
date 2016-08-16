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

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.*;
import lombok.Cleanup;
import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.io.IOUtils;

import org.projectnyx.Nyx;
import org.projectnyx.config.ConfigParser;
import org.projectnyx.network.Client;
import org.projectnyx.network.Protocol;
import org.projectnyx.network.QueryResponse;
import org.projectnyx.network.ext.SessionHandler;
import org.projectnyx.network.ext.SharedServerSocket;
import org.projectnyx.network.ext.SharedServerSocket.Identifier;
import org.projectnyx.network.ext.SharedSocketPool;
import org.projectnyx.network.ext.udp.SharedUdpServerSocket;
import org.projectnyx.network.ext.udp.UdpSessionHandler;
import org.projectnyx.network.xml.ProtocolDeclaration;
import org.projectnyx.properties.Server;

import static org.projectnyx.network.mcpe.mcpe.McpeConsts.APPARENT_PROTOCOL_MAJOR;
import static org.projectnyx.network.mcpe.mcpe.McpeConsts.APPARENT_PROTOCOL_MINOR;
import static org.projectnyx.network.mcpe.mcpe.McpeConsts.APPARENT_VERSION;
import static org.projectnyx.network.mcpe.raknet.RakNetConsts.*;

public class McpeProtocol extends Protocol {
    @Getter(lazy = true) private final long serverId = new Random().nextLong();
    private Set<Identifier> socketsUsed;

    private Map<Byte, ProtocolDeclaration> protocols = new HashMap<>();

    @Override
    @SneakyThrows({IOException.class})
    public void init() {
        socketsUsed = new HashSet<>();
        List<Server> servers = Nyx.getInstance().getConfig().servers.servers;
        servers.forEach(server -> {
            if(!"mcpe".equalsIgnoreCase(server.type)) {
                return;
            }
            Identifier identifier = new Identifier(SharedUdpServerSocket.TYPE, server.ip, server.port);
            socketsUsed.add(identifier);
            if(SharedSocketPool.getInstance().getOrCreate(identifier) == null) {
                throw new AssertionError();
            }
        });

        ClassLoader classLoader = getClass().getClassLoader();
        @Cleanup InputStream listStream = classLoader.getResourceAsStream("protocol/mcpe/protocols.list");
        for(String line : IOUtils.readLines(listStream, Charset.defaultCharset())) {
            if(line.charAt(0) == '#' || line.isEmpty()) {
                continue;
            }
            @Cleanup InputStream is = classLoader.getResourceAsStream("protocol/mcpe/" + line.trim());
            ConfigParser parser = new ConfigParser(is);
                ProtocolDeclaration protocol = (ProtocolDeclaration) parser.parse();
            protocols.put(protocol.version, protocol);
        }
    }

    @Override
    public Client acceptSession(SessionHandler session) {
        if(!(session instanceof UdpSessionHandler) || !usesSocket(session.getSocket())) {
            return null;
        }
        UdpSessionHandler udpSession = (UdpSessionHandler) session;
        Iterator<ByteBuffer> iterator = udpSession.getInitStack().getStack().iterator();
        McpeClient client = null;
        while(iterator.hasNext()) {
            ByteBuffer buffer = iterator.next();
            if(client != null) {
                client.handlePacket(buffer);
                continue;
            }
            switch(buffer.get()) {
                case RAKNET_UNCONNECTED_PING:
                    try {
                        handleUnconnectedPing(udpSession, buffer);
                        iterator.remove();
                    } catch(BufferUnderflowException e) {
                        // TODO ignore
                    }
                    break;
                case RAKNET_OPEN_CONNECTION_REQUEST_1:
                    client = new McpeClient(this, udpSession);
                    break;
            }
        }
        return client;
    }

    @Override
    public String getName() {
        return "MCPE";
    }

    public boolean usesSocket(SharedServerSocket socket) {
        return socketsUsed.contains(socket.getIdentifier());
    }

    private void handleUnconnectedPing(UdpSessionHandler session, ByteBuffer buffer) {
        long pingId = buffer.order(ByteOrder.BIG_ENDIAN).getLong();

        QueryResponse queryResponse = Nyx.getInstance().getNetwork().getQueryResponse();
        String serverNameString = String.format("MCPE;%s;%d %d;%s;%d;%d",
                queryResponse.getName(), APPARENT_PROTOCOL_MAJOR, APPARENT_PROTOCOL_MINOR, APPARENT_VERSION,
                queryResponse.getCountPlayers(), queryResponse.getMaxPlayers());
        byte[] serverName = serverNameString.getBytes();
        ByteBuffer send = ByteBuffer.allocate(1 + 8 + 8 + MAGIC.length + 2 + serverName.length).order(ByteOrder.BIG_ENDIAN);
        send.put(RAKNET_UNCONNECTED_PONG)
                .putLong(pingId)
                .putLong(getServerId())
                .put(MAGIC)
                .putShort((short) serverName.length)
                .put(serverName);
        session.send(buffer);
    }
}
