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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import lombok.SneakyThrows;

import org.projectnyx.network.ByteBufferReader;

public abstract class ReceivedPacket extends ByteBufferReader implements Cloneable {
    public void parse(ByteBuffer buffer) {
        this.buffer = buffer;
        buffer.order(ByteOrder.BIG_ENDIAN);

        decode();
    }

    public abstract byte pid();

    protected abstract void decode();

    @SneakyThrows(CloneNotSupportedException.class)
    public ReceivedPacket getClone() {
        return (ReceivedPacket) clone();
    }
}
