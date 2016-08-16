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
package org.projectnyx.properties;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.SneakyThrows;
import lombok.ToString;

import org.projectnyx.config.ConfigElement;

@ToString
public class Server extends ConfigElement {
    /**
     * The type of protocol that listens on this socket
     */
    public String type;
    /**
     * The IP address (default {@code 0.0.0.0})
     */
    public InetAddress ip;
    /**
     * The port to listen on
     */
    public int port;

    @SneakyThrows({UnknownHostException.class})
    public Server() {
        ip = InetAddress.getByAddress(new byte[4]);
    }
}
