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
/**
 * <p>Key classes:</p>
 *
 * <table summary="org.projectnyx.network key classes">
 *
 * <tr><th>Class name</th><th>Structure</th><th>Scope</th> <th>Content</th></tr>
 *
 * <tr><td>{@link org.projectnyx.network.NetworkManager}</td><td>Singleton</td><td>All networks</td><td>Module
 * management</td></tr>
 *
 * <tr><td>{@link org.projectnyx.network.Protocol}</td><td>Abstract<br>modular</td><td>All clients for that protocol
 * </td><td>A protocol type</td></tr>
 *
 * <tr><td>{@link org.projectnyx.network.Client}</td><td>Abstract<br>Instantiated by {@link
 * org.projectnyx.network.Protocol}</td><td>Per client</td><td>Protocol conversion</td></tr>
 *
 * <tr><td>{@link org.projectnyx.network.ext.SharedServerSocket}</td><td>Abstract<br>Inflexible
 * implementations</td><td>All clients through that server socket</td><td>Internet-level protocol</td></tr>
 *
 * <tr><td>{@link org.projectnyx.network.ext.SessionHandler}</td><td>Abstract<br>Instantiated by {@link
 * org.projectnyx.network.ext.SharedServerSocket}</td><td>Per client</td><td>Internet-level protocol</td></tr>
 *
 * <tr><td>{@link org.projectnyx.player.Player}<br>(not part of this package)</td><td>Instance</td><td>One server-side
 * player from one or multiple clients</td> <td>Game-level mechanism</td></tr>
 *
 * </table>
 *
 * <h3>Login procedure</h3>
 *
 * <ol> <li>Data enter through one of the {@link org.projectnyx.network.ext.SharedServerSocket}s
 *
 * <ul> <li>UDP: Read into {@link org.projectnyx.network.ext.udp.UdpBufferThread}, then received by {@link
 * org.projectnyx.network.ext.udp.SharedUdpServerSocket} through ticking</li>
 *
 * <li>TCP: To be implemented</li> </ul>
 *
 * </li>
 *
 * <li>{@link org.projectnyx.network.NetworkManager#adoptSession(org.projectnyx.network.ext.SessionHandler)} is called
 * per packet read to look for a suitable protocol. If no protocol is available, login packets are stacked. If a
 * protocol accepted this client, a protocol-dependent {@link org.projectnyx.network.Client} object is created by the
 * protocol.</li>
 *
 * <li>Subsequent packets are handled by the client directly.</li>
 *
 * <li>When enough data have been created, a {@link org.projectnyx.player.Player} object is created or assigned
 * to act as the in-game entity controlled by this client (and other clients).</li>
 *
 * </ol>
 */
package org.projectnyx.network;
