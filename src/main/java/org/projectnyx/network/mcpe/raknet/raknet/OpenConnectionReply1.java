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
package org.projectnyx.network.mcpe.raknet.raknet;

import org.projectnyx.network.mcpe.SentPacket;
import org.projectnyx.network.mcpe.raknet.RakNetConsts;

public class OpenConnectionReply1 extends SentPacket {
    public long serverId;
    public short mtuSize;

    @Override
    public byte pid() {
        return RakNetConsts.RAKNET_OPEN_CONNECTION_REPLY_1;
    }

    @Override
    protected int length() {
        return 27;
    }

    @Override
    protected void encode() {
        putBytes(RakNetConsts.MAGIC);
        putLong(serverId);
        putByte((byte) 0);
        putShort(mtuSize);
    }
}
