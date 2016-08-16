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

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.Getter;

import org.projectnyx.network.ext.SessionHandler;
import org.projectnyx.network.mcpe.PingStats;
import org.projectnyx.player.Player;

/**
 * An object
 */
public abstract class Client {
    @Getter private Player assignedPlayer;
    @Getter private PingStats pingStats;
    @Getter private long lastAlive;

    protected Client() {
        refreshActive();
    }

    public abstract Protocol getSourceProtocol();

    public abstract SessionHandler getSessionHandler();

    public abstract void handlePacket(ByteBuffer buffer);

    public void close(CloseReason closeReason) {
        getSessionHandler().close();
        if(assignedPlayer != null) {
            assignedPlayer.detachClient(this);
        }
    }

    public void tick() {
        if(System.currentTimeMillis() - lastAlive > 10e+3){
            close(CloseReason.CLIENT_TIMEOUT);
        }
    }

    public InetSocketAddress getAddress() {
        return getSessionHandler().getAddress();
    }

    public void refreshActive() {
        lastAlive = System.currentTimeMillis();
    }

    public enum CloseReason {
        CLIENT_DISCONNECT,
        CLIENT_TIMEOUT,
        SERVER_DISCONNECT
    }
}
