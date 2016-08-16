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
package org.projectnyx.network.mcpe.raknet;

import java.nio.ByteBuffer;

import org.projectnyx.network.mcpe.ReceivedPacket;

public class RawDataPacket {
    public final static byte RELIABILITY_UNRELIABLE = (byte) 0x00;
    public final static byte RELIABILITY_UNRELIABLE_SEQUENCED = (byte) 0x01;
    public final static byte RELIABILITY_RELIABLE = (byte) 0x02;
    public final static byte RELIABILITY_RELIABLE_ORDERED = (byte) 0x03;
    public final static byte RELIABILITY_RELIABLE_SEQUENCED = (byte) 0x04;
    public final static byte RELIABILITY_UNRELIABLE_WITH_ACK_RECEIPT = (byte) 0x05;
    public final static byte RELIABILITY_UNRELIABLE_SEQUENCED_WITH_ACK_RECEIPT = (byte) 0x06;
    public final static byte RELIABILITY_RELIABLE_WITH_ACK_RECEIPT = (byte) 0x07;
    public final static byte RELIABILITY_RELIABLE_ORDERED_WITH_ACK_RECEIPT = (byte) 0x08;
    public final static byte RELIABILITY_RELIABLE_SEQUENCED_WITH_ACK_RECEIPT = (byte) 0x09;

    public byte[] buffer;
    public byte reliability;
    public boolean hasSplit;
    public int orderIndex;
    public int messageIndex;
    public byte orderChannel;
    public int splitCount;
    public short splitId;
    public int splitIndex;

    public static RawDataPacket read(ReceivedPacket packet) {
        RawDataPacket instance = new RawDataPacket();
        byte flags = packet.getByte();
        instance.reliability = (byte) ((flags & 0b11100000) >>> 5);
        instance.hasSplit = (flags & 0b00010000) > 0;
        instance.buffer = new byte[(int) Math.ceil(packet.getShort() / 8d)];

        switch(instance.reliability) {
            case 1:
                instance.orderIndex = packet.getLTriad();
                instance.orderChannel = packet.getByte();
                break;
            case 2:
                instance.messageIndex = packet.getLTriad();
                break;
            case 3:
            case 4:
                instance.messageIndex = packet.getLTriad();
                instance.orderIndex = packet.getLTriad();
                instance.orderChannel = packet.getByte();
                break;
            case 6:
            case 7:
                instance.messageIndex = packet.getLTriad();
                break;
        }

        if(instance.hasSplit) {
            instance.splitCount = packet.getInt();
            instance.splitId = packet.getShort();
            instance.splitIndex = packet.getInt();
        }

        packet.getBuffer().get(instance.buffer);

        return instance;
    }

    public byte[] write() {
        ByteBuffer bb = ByteBuffer.allocate(binaryLength());
        bb.put((byte) ((reliability << 5) | (hasSplit ? 0b00010000 : 0))).putShort((short) (buffer.length << 3));
        switch(reliability) {
            case 1:
                putLTriad(bb, orderIndex);
                bb.put(orderChannel);
                break;
            case 2:
                putLTriad(bb, messageIndex);
                break;
            case 3:
            case 4:
                putLTriad(bb, messageIndex);
                putLTriad(bb, orderIndex);
                bb.put(orderChannel);
                break;
            case 6:
            case 7:
                putLTriad(bb, messageIndex);
                break;
        }
        if(hasSplit) {
            bb.putInt(splitCount).putShort(splitId).putInt(splitIndex);
        }
        bb.put(buffer);

        return bb.array();
    }

    public int binaryLength() {
        int relLen = 0;
        switch(reliability) {
            case 1:
            case 2:
            case 6:
            case 7:
                relLen = 3;
                break;
            case 3:
            case 4:
                relLen = 6;
                break;
        }
        return 3 + buffer.length + (hasSplit ? 10 : 0) + relLen;
    }

    public static ByteBuffer putLTriad(ByteBuffer buffer, int triad) {
        buffer.put((byte) (triad & 0xFF));
        buffer.put((byte) ((triad >> 8) & 0xFF));
        buffer.put((byte) ((triad >> 16) & 0xFF));
        return buffer;
    }
}
