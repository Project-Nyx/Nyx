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
package org.projectnyx.network.mcpe;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class SentPacket {
    @Getter(AccessLevel.PROTECTED) private ByteBuffer buffer;

    public ByteBuffer write() {
        buffer = ByteBuffer.allocate(length() + 1);
        buffer.put(getId());
        encode();
        buffer.flip();
        return buffer;
    }

    public abstract byte getId();

    /**
     * Returns the length of this packet excluding the packet ID
     *
     * @return length without packet ID
     */
    protected abstract int length();

    protected abstract void encode();

    protected void putByte(byte value) {
        buffer.put(value);
    }

    protected void putShort(short value) {
        buffer.putShort(value);
    }

    protected void putInt(int value) {
        buffer.putInt(value);
    }

    protected void putLong(long value) {
        buffer.putLong(value);
    }

    protected void putFloat(float value) {
        buffer.putFloat(value);
    }

    protected void putDouble(double value) {
        buffer.putDouble(value);
    }

    protected void putBytes(byte[] value) {
        buffer.put(value);
    }

    protected void putString(String value) {
        buffer.putShort((short) value.length());
        buffer.put(value.getBytes());
    }

    protected void putAddress(InetSocketAddress address) {
        byte[] bytes = address.getAddress().getAddress();
        if(bytes.length == 4) {
            putByte((byte) 4);
            putBytes(new byte[]{
                    (byte) ~bytes[0],
                    (byte) ~bytes[1],
                    (byte) ~bytes[2],
                    (byte) ~bytes[3],
            });
        } else {
            throw new UnsupportedOperationException();
        }
        putShort((short) address.getPort());
    }

    protected void putLTriad(int value) {
        buffer.put((byte) (value & 0xFF));
        buffer.put((byte) ((value >> 8) & 0xFF));
        buffer.put((byte) ((value >> 16) & 0xFF));
    }
}
