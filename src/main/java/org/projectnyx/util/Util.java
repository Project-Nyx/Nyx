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
package org.projectnyx.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.JarFile;
import lombok.Cleanup;

import org.projectnyx.Nyx;

public class Util {
    public static Properties loadJar(File file) throws IOException {
        JarFile jar = new JarFile(file);
        Properties properties = new Properties();
        @Cleanup InputStream inputStream = jar.getInputStream(jar.getJarEntry("mod.properties"));
        properties.load(inputStream);
        String className = properties.getProperty("mainClass");
        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, Nyx.class.getClassLoader());
        try {
            loader.loadClass(className);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("Cannot load main class");
        }

        return properties;
    }
}
