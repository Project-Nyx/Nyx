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

import java.nio.ByteBuffer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * Parent class for two-direction MCPE data packets
 */
public abstract class DataPacket {
    @Getter @Setter private ByteBuffer buffer;

    public abstract byte getPacketId();

    public void write() {
        throw new UnsupportedOperationException("Packet is client->server only");
    }

    public DataPacket parse(){
        read();
        return this;
    }
    public void read() {
        throw new UnsupportedOperationException("Packet is server->client only");
    }

    public void writeByte(byte b) {
        buffer.put(b);
    }

    public void writeShort(short s) {
        buffer.order(BIG_ENDIAN).putShort(s);
    }

    public void writeTriad(int t) {
        buffer.put((byte) ((t >> 16) & 0xFF))
                .put((byte) ((t >> 8) & 0xFF))
                .put((byte) (t & 0xFF));
    }

    public void writeInt(int i) {
        buffer.order(BIG_ENDIAN).putInt(i);
    }

    public void writeLong(long l) {
        buffer.order(BIG_ENDIAN).putLong(l);
    }

    public void writeFloat(float f) {
        buffer.order(BIG_ENDIAN).putFloat(f);
    }

    public void writeDouble(double d) {
        buffer.order(BIG_ENDIAN).putDouble(d);
    }

    public void writeLShort(short s) {
        buffer.order(LITTLE_ENDIAN).putShort(s);
    }

    public void writeLTriad(int t) {
        buffer.put((byte) (t & 0xFF))
                .put((byte) ((t >> 8) & 0xFF))
                .put((byte) ((t >> 16) & 0xFF));
    }

    public void writeLInt(int i) {
        buffer.order(LITTLE_ENDIAN).putInt(i);
    }

    public void writeLLong(long l) {
        buffer.order(LITTLE_ENDIAN).putLong(l);
    }

    public void writeLFloat(float f) {
        buffer.order(LITTLE_ENDIAN).putFloat(f);
    }

    public void writeLDouble(double d) {
        buffer.order(LITTLE_ENDIAN).putDouble(d);
    }

    public void writeBytes(byte[] bytes) {
        buffer.put(bytes);
    }

    public void writeBytes(ByteBuffer bytes) {
        buffer.put(bytes);
    }

    public byte readByte() {
        return buffer.get();
    }

    public short readShort() {
        return buffer.order(BIG_ENDIAN).getShort();
    }

    public int readTriad() {
        return (buffer.get() << 16) | (buffer.get() << 8) | buffer.get();
    }

    public int readInt() {
        return buffer.order(BIG_ENDIAN).getInt();
    }

    public long readLong() {
        return buffer.order(BIG_ENDIAN).getLong();
    }

    public float readFloat() {
        return buffer.order(BIG_ENDIAN).getFloat();
    }

    public double readDouble() {
        return buffer.order(BIG_ENDIAN).getDouble();
    }

    public short readLShort() {
        return buffer.order(LITTLE_ENDIAN).getShort();
    }

    public int readLTriad() {
        return buffer.get() | (buffer.get() << 8) | (buffer.get() << 16);
    }

    public int readLInt() {
        return buffer.order(LITTLE_ENDIAN).getInt();
    }

    public long readLLong() {
        return buffer.order(LITTLE_ENDIAN).getLong();
    }

    public float readLFloat() {
        return buffer.order(LITTLE_ENDIAN).getFloat();
    }

    public double readLDouble() {
        return buffer.order(LITTLE_ENDIAN).getDouble();
    }

    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    @Override
    @SneakyThrows(CloneNotSupportedException.class)
    public DataPacket clone() {
        return (DataPacket) super.clone();
    }
}
