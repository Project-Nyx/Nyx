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
package org.projectnyx.network;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.projectnyx.module.ModuleManager;
import org.projectnyx.network.ext.SessionHandler;
import org.projectnyx.network.ext.SharedSocketPool;

public class NetworkManager extends ModuleManager<Protocol> {
    @Getter private static NetworkManager instance;

    @Getter @Setter @NotNull private QueryResponse queryResponse;
    private final SharedSocketPool sockets = new SharedSocketPool();

    private boolean running = true;

    public NetworkManager() {
        super(Protocol.class);
        instance = this;

        queryResponse = new QueryResponse();
        queryResponse.setName("Nyx rised.");
        queryResponse.setCountPlayers(1);
        queryResponse.setMaxPlayers(10);
    }

    public void start() {
        new Thread(() -> {
            while(running) {
                sockets.tick();
            }
        }).start();
    }

    @Override
    protected String moduleType() {
        return "Protocol";
    }

    /**
     * Requires a protocol to adopt this session.
     *
     * @param session
     * @return
     */
    public Client adoptSession(SessionHandler session) {
        for(Protocol protocol : getModules().values()) {
            Client client = protocol.acceptSession(session);
            if(client != null) {
                return client;
            }
        }
        return null;
    }

    public void stop() {
        running = false;
    }
}
