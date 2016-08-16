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
 * <h2>Terms</h2>
 *
 * <p>RakNet packet = UDP packet = A low-level packet that is sent through the UDP layer directly</p>
 *
 * <p>Data packet group = A certain type of RakNet packet (actually packet IDs 0x80 - 0x8F) that contains a group of Raw
 * data packets, ordered by a sequence number triad</p>
 *
 * <p>Raw data packet = A mid-level packet sent in groups through a Data packet group, a.k.a. Encapsulated Packet in
 * RakLib by PocketMine Team</p>
 */
package org.projectnyx.network.mcpe.raknet;
