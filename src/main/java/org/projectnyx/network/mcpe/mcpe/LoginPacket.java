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
package org.projectnyx.network.mcpe.mcpe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;
import lombok.SneakyThrows;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class LoginPacket extends DataPacket {
    public int protocol;

    @Override
    public byte getPacketId() {
        return McpeConsts.LoginPacket;
    }

    @Override
    @SneakyThrows({IOException.class})
    public void read() {
        protocol = readInt();
        byte[] deflated = readBytes(Math.min(readLInt(), 64 << 20));
        setBuffer(ByteBuffer.wrap(IOUtils.toByteArray(new InflaterInputStream(new ByteArrayInputStream(deflated)))));

        byte[] json = readBytes(readLInt());
        System.err.println(new String(json));
        JSONObject object = new JSONObject();
    }
}
