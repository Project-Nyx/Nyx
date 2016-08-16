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
package org.projectnyx.network.xml;

import java.util.List;

import org.projectnyx.config.DefaultPackage;
import org.projectnyx.token.request.Request;
import org.projectnyx.token.response.Response;

public class Packet {
    public byte id;
    @DefaultPackage("org.projectnyx.token.request") public List<Class<? extends Request>> tokenRequests;
    @DefaultPackage("org.projectnyx.token.response") public List<Class<? extends Response>> tokenResponses;

    public Structure structure;
}
