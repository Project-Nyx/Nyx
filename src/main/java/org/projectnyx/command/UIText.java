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
package org.projectnyx.command;

import org.apache.commons.lang3.StringEscapeUtils;

public interface UIText {
    /**
     * <p>Returns a plain text preprocessed for a specific command issuer.</p>
     * <p>A common usage of the issuer parameter is to translate to that issuer's locale.</p>
     * <p>This method should return a consistent value if {@code null} is passed in.</p>
     *
     * @param issuer the {@link CommandIssuer} whom this text will be preprocessed for. If {@code null}, a default value
     *               should be returned.
     * @return unformatted text
     */
    public String get(CommandIssuer issuer);

    /**
     * <p>Returns an HTML-encoded text for a specific command issuer.</p>
     *
     * <p>The default implementation redirects to {@link #get(CommandIssuer)} and escapes special HTML characters in
     * it.</p>
     *
     * @param issuer the {@link CommandIssuer} to preprocess for or {@code null}.
     * @return HTML-encoded text
     * @see #get(CommandIssuer)
     */
    public default String asHTML(CommandIssuer issuer) {
        return StringEscapeUtils.escapeHtml4(get(issuer));
    }

    /**
     * <p>Returns a text encoded with Minecraft color codes (<code>&sect;*</code>).</p>
     * <p>The default implementation redirects to {@link #get(CommandIssuer)}.</p>
     *
     * @param issuer the {@link CommandIssuer} to preprocess for or {@code null}.
     * @return A text formatted using MCPE color codes
     * @see #get(CommandIssuer)
     */
    public default String toMinecraft(CommandIssuer issuer) {
        return get(issuer);
    }

    public default String toANSI(CommandIssuer issuer) {
        return get(issuer);
    }
}
