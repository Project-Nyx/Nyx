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

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NonNull;

import org.projectnyx.network.ext.SharedServerSocket.Identifier;

public class SharedSocketPool {
    @Getter private static SharedSocketPool instance;

    private Map<Identifier, SharedServerSocket> sockets = new HashMap<>();

    public SharedSocketPool() {
        instance = this;
    }

    @NonNull
    public SharedServerSocket getOrCreate(Identifier identifier) {
        if(!sockets.containsKey(identifier)) {
            addSocket(identifier.create());
        }
        return sockets.get(identifier);
    }

    void addSocket(SharedServerSocket socket) {
        sockets.put(socket.getIdentifier(), socket);
    }

    void removeSocket(SharedServerSocket socket) {
        sockets.remove(socket.getIdentifier(), socket);
    }

    public void tick() {
        sockets.values().forEach(SharedServerSocket::tick);
    }
}
