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

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.projectnyx.network.Protocol;

@RequiredArgsConstructor
public abstract class SessionHandler {
    @Getter private final InetSocketAddress address;
    @Getter private final SharedServerSocket socket;

    public abstract void onReceive(PacketWithAddress pk);

    public abstract void send(ByteBuffer buffer);

    public abstract void close();

    public abstract Protocol getProtocol();

    public void tick() {

    }

    /**
     * <p>Returns whether this session an orphan.</p>
     *
     * <p>An orphan session is a session that has not been recognized by any sessions yet.</p>
     *
     * @return whether this session is an orphan.
     */
    public abstract boolean isOrphan();
}
