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
package org.projectnyx.config;

import java.io.*;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import org.projectnyx.properties.NyxConfig;

import static org.xmlpull.v1.XmlPullParser.*;

/**
 * The class for parsing a Nyx-style XML config file.
 */
public class ConfigParser {
    private final Reader input;
    private final XmlPullParser parser;
    @Getter @Setter private String packageContext = NyxConfig.class.getPackage().getName();
    private ConfigElement currentElement = null;
    private String currentAttribute = null;
    private String currentTextBuffer = null;

    /**
     * Creates a parser on a Nyx-style XML config file.
     *
     * @param file The file to read contents from
     * @throws IOException if the file cannot be opened for reading
     */
    public ConfigParser(File file) throws IOException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a parser that reads a Nyx-style XML config from an {@link InputStream}
     *
     * @param is The stream to read config from
     */
    public ConfigParser(InputStream is) {
        this(new InputStreamReader(is));
    }

    /**
     * Creates a parser that reads a Nyx-style XML config from a {@link Reader}
     *
     * @param reader The reader to read config from
     */
    @SneakyThrows({XmlPullParserException.class})
    public ConfigParser(Reader reader) {
        parser = XmlPullParserFactory.newInstance().newPullParser();
        parser.setInput(input = reader);
    }

    /**
     * Parses the config and returns the config root.
     *
     * @return the root config element that this config holds
     * @throws NumberFormatException if any config formatting errors are encountered, or if a config entry has a
     *                               mismatched type (e.g. a supposedly integer field is not a number)
     * @throws IOException           if an I/O error occurs
     * @see org.projectnyx.config
     */
    public ConfigElement parse() throws NumberFormatException, IOException {
        try {
            int eventType = parser.getEventType();
            while(eventType != END_DOCUMENT) {
                switch(eventType) {
                    case START_DOCUMENT:
                        break;
                    case START_TAG:
                        startTag();
                        break;
                    case TEXT:
                        readText();
                        break;
                    case END_TAG:
                        endTag();
                        break;
                }
                eventType = parser.next();
            }

            assert currentElement != null;
            return currentElement;
        } catch(XmlPullParserException e) {
            throw new NumberFormatException(e.getLocalizedMessage());
        }finally{
            input.close();
        }
    }

    private void startTag() {
        assert currentAttribute == null;

        if(currentElement == null) {
            String pk = parser.getAttributeValue(null, "context");
            if(pk != null) {
                packageContext = pk;
            }
        }

        String name = parser.getName();
        Class<? extends ConfigElement> child = computeClassName(name, packageContext);
        if(child == null) {
            startAttributeTag();
            return;
        }

        ConfigElement newElement;
        try {
            newElement = child.newInstance();
            newElement.setParent(currentElement);
//            newElement = child.getConstructor(ConfigElement.class).newInstance(currentElement);
        } catch(IllegalAccessException | InstantiationException | ExceptionInInitializerError e) {
            throw new RuntimeException(e);
        }
        if(currentElement != null) {
            currentElement.addElement(newElement);
        }
        currentElement = newElement;
        for(int i = 0; i < parser.getAttributeCount(); i++) {
            currentElement.addAttribute(parser.getAttributeName(i), parser.getAttributeValue(i));
        }
    }

    private void startAttributeTag() {
        if(currentElement == null) {
            throw new NumberFormatException("Root element is not a ConfigElement class");
        }
        currentAttribute = parser.getName();
        currentTextBuffer = "";
    }

    public static Class<? extends ConfigElement> computeClassName(String name, String packageContext) {
        int dotIndex = name.indexOf('.');
        String className;
        if(dotIndex > 0) {
            className = name;
        } else {
            String packageName = packageContext;
            if(dotIndex == -1) {
                packageName += ".";
            }
            className = packageName + name;
        }

        try {
            return Class.forName(className).asSubclass(ConfigElement.class);
        } catch(ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }

    private void readText() {
        if(currentTextBuffer == null) {
            return;
        }

        currentTextBuffer += parser.getText();
    }

    private void endTag() {
        if(currentAttribute != null && currentTextBuffer != null) {
            currentElement.addAttribute(currentAttribute, currentTextBuffer);
            currentAttribute = null;
            currentTextBuffer = null;
            return;
        }
        ConfigElement parent = currentElement.getParent();
        if(parent != null) {
            currentElement = parent;
        }
    }
}
