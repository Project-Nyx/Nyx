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
/**
 * <p>This package reads XML-encoded configs whose structure is reflected by runtime Java classes.</p>
 *
 * <p>Each XML element in the config either represents an object (reflected by a class that <code>extends {@link
 * org.projectnyx.config.ConfigElement}</code>) or a property (reflected by a class field).</p>
 *
 * <p>When an XML element is read, a class for its name is searched using the following rules:</p>
 *
 * <ul>
 *
 * <li>If the tag name does not contain any dots ({@code .}), prepend the context package (specified by a {@code
 * context} attribute in the root element, otherwise defaults to the {@link org.projectnyx.properties} package, or set
 * programmatically using {@link org.projectnyx.config.ConfigParser#setPackageContext(java.lang.String)})<br>
 *
 * e.g. {@code <NyxConfig>} resolves to {@code org.projectnyx.properties.NyxConfig}</li>
 *
 * <li>If the tag name starts with a dot, the context package is prepended.<br> e.g. {@code <.Foo.Bar>} resolves to
 * {@code org.projectnyx.properties.Foo.Bar}</li>
 *
 * <li>If the tag name does not start with a dot but contains dots, it is used as the class name directly.</li> </ul>
 *
 * <p>If such class exists and it extends {@link org.projectnyx.config.ConfigElement}, an instance of this class will be
 * created and added to the its parent element (if any). The parser will search through the <em>declared</em> fields
 * (not any inherited fields) to find a suitable {@link java.util.List} field that this element can be added into. If no
 * available fields are found, it will be added into {@link org.projectnyx.config.ConfigElement#getOtherChildren()}.</p>
 *
 * <p>If this class does not exist, it will be parsed as an attribute instead. (If it is the root element, an error will
 * be thrown) If a field in the current element has the same name as this tag's name, the current field will be
 * converted into the type required by this field. If no such field is found, the string will be added as-is into
 * {@link org.projectnyx.config.ConfigElement#getOtherAttributes()}.</p>
 */
package org.projectnyx.config;
