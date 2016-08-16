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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.projectnyx.network.mcpe.SentPacket;
import org.projectnyx.network.mcpe.raknet.RakNetConsts;

@RequiredArgsConstructor
public class SentAckNack extends SentPacket {
    public final static boolean ACK = true;
    public final static boolean NACK = false;

    private final boolean ack;

    @Getter private List<Integer> seqNumbers = new ArrayList<>();
    private short groupCount = -1;
    private byte[] encoded = null;

    @Override
    public byte getId() {
        return ack ? RakNetConsts.ACK : RakNetConsts.NACK;
    }

    @Override
    protected int length() {
        return 2 + getEncoded().length;
    }

    @Override
    protected void encode() {
        putShort(groupCount);
        putBytes(getEncoded());
    }

    @NonNull
    @SneakyThrows({IOException.class})
    protected byte[] getEncoded() {
        if(encoded != null) {
            return encoded;
        }

        seqNumbers.sort((i1, i2) -> Integer.signum(i1 - i2));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        int startCont = seqNumbers.get(0);
        int last = startCont;
        groupCount = 1;
        for(Integer packet : seqNumbers) {
            if(last + 1 == packet) {
                last = packet;
            } else {
                if(startCont == last) {
                    dos.writeByte(1);
                    writeLTriad(dos, last);
                } else {
                    dos.writeByte(0);
                    writeLTriad(dos, startCont);
                    writeLTriad(dos, last);
                }
                startCont = last = packet;
                groupCount++;
            }
        }
        if(startCont == last) {
            dos.writeByte(1);
            writeLTriad(dos, last);
        } else {
            dos.writeByte(0);
            writeLTriad(dos, startCont);
            writeLTriad(dos, last);
        }

        return encoded = baos.toByteArray();
    }

    private void writeLTriad(DataOutputStream dos, int value) throws IOException {
        dos.writeByte((byte) (value & 0xFF));
        dos.writeByte((byte) ((value >> 8) & 0xFF));
        dos.writeByte((byte) ((value >> 16) & 0xFF));
    }
}
