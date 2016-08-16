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
package org.projectnyx.module;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

import org.projectnyx.Nyx;

/**
 * <p>Abstract superclass for module managers.</p>
 *
 * <p>Definition of a module: something that adds a certain type of flexible functionality to a server. This does not
 * include basic utilities like config parsers, tickers, etc., because they are inflexible and cannot be removed.</p>
 *
 * @param <T> The type that this module manages.
 */
public abstract class ModuleManager<T> {
    private final Class<T> myClass;
    @Getter(lazy = true) private final String moduleTypeName = moduleType();
    @Getter private Map<String, T> modules = new HashMap<>();

    protected abstract String moduleType();

    protected ModuleManager(Class<T> myClass) {
        this.myClass = myClass;
        Nyx.getInstance().getModuleManagers().put(getModuleTypeName(), this);
    }

    public void loadForClass(String name, String className) throws Exception {
        Class<? extends T> aClass = Class.forName(className).asSubclass(myClass);
        T instance = aClass.newInstance();
        modules.put(name, instance);
    }
}
