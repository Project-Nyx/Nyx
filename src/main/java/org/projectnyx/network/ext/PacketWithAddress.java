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
package org.projectnyx.network.ext;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import lombok.Getter;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import org.projectnyx.Nyx;

/**
 * <p>A set of packet and address.</p>
 */
public class PacketWithAddress {
    @Getter private final ByteBuffer buffer;
    @Getter private final InetSocketAddress address;

    /**
     * The buffer will be automatically flipped if the position is not at 0
     *
     * @param buffer  the buffer of data to send, from 0 to limit if position is 0, or from 0 to position if position is
     *                not 0
     * @param address the target address of the client. can be null in some network implementations.
     */
    public PacketWithAddress(ByteBuffer buffer, InetSocketAddress address) {
        this(buffer, address, buffer.position() != 0);
    }

    public PacketWithAddress(ByteBuffer buffer, InetSocketAddress address, boolean flip) {
        if(flip) {
            if(buffer.position() == 0) {
                Nyx.getLog().warn("Attempt to flip buffer with zero length", new Throwable("Backtrace"));
            }
            if(buffer.array().length >= (buffer.position() << 3)) { // save some memory
                buffer = ByteBuffer.wrap(ArrayUtils.subarray(buffer.array(), 0, buffer.position()));
            } else {
                buffer.flip();
            }
        }
        this.buffer = buffer;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Data from address " + address.toString() + ": " + Hex.encodeHexString(buffer.array());
    }
}
