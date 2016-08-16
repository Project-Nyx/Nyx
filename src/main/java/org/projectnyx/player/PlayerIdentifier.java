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
package org.projectnyx.player;

import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * A unique identifier for this player
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public class PlayerIdentifier {
    @NotNull private final String prefix;
    private final String name;

    public PlayerIdentifier(String name) {
        this(null, name);
    }

    @Override
    public String toString() {
        return prefix == null ? name : (prefix + ";" + name);
    }
}
