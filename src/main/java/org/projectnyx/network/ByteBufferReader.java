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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import lombok.Getter;
import lombok.SneakyThrows;

public class ByteBufferReader {
    @Getter protected ByteBuffer buffer;

    public byte getByte() {
        return buffer.get();
    }

    public short getShort() {
        return buffer.getShort();
    }

    public int getLTriad() {
        byte a = getByte();
        byte b = getByte();
        byte c = getByte();
        return a | (b << 8) | (c << 16);
    }

    public int getInt() {
        return buffer.getInt();
    }

    public long getLong() {
        return buffer.getLong();
    }

    public float getFloat() {
        return buffer.getFloat();
    }

    public double getDouble() {
        return buffer.getDouble();
    }

    public byte[] getBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public String getString() {
        short length = getShort();
        return new String(getBytes(length));
    }

    @SneakyThrows(UnknownHostException.class)
    public InetSocketAddress getAddress() {
        switch(getByte()) {
            case 4:
                byte[] bytes = {
                        (byte) (~getByte() & 0xFF),
                        (byte) (~getByte() & 0xFF),
                        (byte) (~getByte() & 0xFF),
                        (byte) (~getByte() & 0xFF),
                };
                short port = getShort();
                return new InetSocketAddress(InetAddress.getByAddress(bytes), port);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
