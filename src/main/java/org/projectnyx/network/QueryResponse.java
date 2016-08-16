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

import lombok.Data;

/**
 * This class stores the response generated for queries to the server.
 */
@Data
public class QueryResponse {
    /**
     * Server name
     */
    private String name;
    /**
     * Number of online players
     */
    private int countPlayers;
    /**
     * Number of slots (soft limit)
     */
    private int maxPlayers;
}
