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

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * Superclass for all config element tags.
 *
 * @see org.projectnyx.config
 */
@RequiredArgsConstructor
public class ConfigElement {
    @Getter @Setter private ConfigElement parent;
    @Getter private Map<String, ConfigElement> otherChildren = new LinkedHashMap<>();
    @Getter private Map<String, String> otherAttributes = new LinkedHashMap<>();

    @SneakyThrows({IllegalAccessException.class})
    @SuppressWarnings("unchecked")
    public void addAttribute(String name, String value) throws NumberFormatException {
        Field field;
        try {
            field = getClass().getDeclaredField(name);
        } catch(NoSuchFieldException e) {
            otherAttributes.put(name, value);
            return;
        }
        Class<?> type = field.getType();
        List list = null;
        if(type.isAssignableFrom(ArrayList.class)) {
            Type genericType = field.getGenericType();
            if(genericType instanceof ParameterizedType) {
                Type[] typeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
                assert typeArgs.length == 1;

                try {
                    type = Class.forName(typeArgs[0].getTypeName());
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                list = (List) field.get(this);
                if(list == null) {
                    field.set(this, list = new ArrayList());
                }
            }
        }

        String trimmed = value.trim();
        String number = trimmed;
        int radix = 10;
        try {
            if(trimmed.startsWith("0x")) {
                radix = 16;
                number = trimmed.substring(2);
            } else if(trimmed.startsWith("0b")) {
                radix = 2;
                number = trimmed.substring(2);
            } else if(trimmed.startsWith("0")) {
                radix = 8;
                number = trimmed.substring(1);
            }
        } catch(StringIndexOutOfBoundsException e) {
            throw new NumberFormatException(e.getLocalizedMessage());
        }

        if(type.isAssignableFrom(String.class)) {
            if(list != null) {
                list.add(value);
            } else {
                field.set(this, value);
            }
        } else if(type.isAssignableFrom(int.class)) {
            int i = Integer.parseInt(number, radix);
            if(list != null) {
                list.add(i);
            } else {
                field.setInt(this, i);
            }
        } else if(type.isAssignableFrom(byte.class)) {
            byte i = Byte.parseByte(number, radix);
            if(list != null) {
                list.add(i);
            } else {
                field.setByte(this, i);
            }
        } else if(type.isAssignableFrom(short.class)) {
            short i = Short.parseShort(number, radix);
            if(list != null) {
                list.add(i);
            } else {
                field.setShort(this, i);
            }
        } else if(type.isAssignableFrom(long.class)) {
            long i = Long.parseLong(number, radix);
            if(list != null) {
                list.add(i);
            } else {
                field.setLong(this, i);
            }
        } else if(type.isAssignableFrom(boolean.class)) {
            boolean i = "on".equalsIgnoreCase(value) ||
                    "true".equalsIgnoreCase(value) ||
                    "yes".equalsIgnoreCase(value) ||
                    "y".equalsIgnoreCase(value);
            if(list != null) {
                list.add(i);
            } else {
                field.setBoolean(this, i);
            }
        } else if(type.isAssignableFrom(float.class)) {
            float i = Float.parseFloat(trimmed);
            if(list != null) {
                list.add(i);
            } else {
                field.setFloat(this, i);
            }
        } else if(type.isAssignableFrom(double.class)) {
            double i = Double.parseDouble(trimmed);
            if(list != null) {
                list.add(i);
            } else {
                field.setDouble(this, i);
            }
        } else if(type.isAssignableFrom(char.class)) {
            char i = trimmed.length() > 0 ? trimmed.charAt(0) : value.length() > 0 ? value.charAt(0) : ' ';
            if(list != null) {
                list.add(i);
            } else {
                field.setChar(this, i);
            }
        } else if(type.isAssignableFrom(InetAddress.class)) {
            InetAddress i;
            try {
                i = InetAddress.getByName(trimmed);
            } catch(UnknownHostException e) {
                throw new NumberFormatException(e.getLocalizedMessage());
            }
            if(list != null) {
                list.add(i);
            } else {
                field.set(this, i);
            }
        } else if(type.isAssignableFrom(Class.class)) {
            DefaultPackage annotation = field.getDeclaredAnnotation(DefaultPackage.class);
            Set<String> packages = new HashSet<>();
            packages.add("java.lang");
            if(annotation != null) {
                packages.addAll(Arrays.asList(annotation.value()));
            }

            Class<?> i = null;
            // TODO implement
            if(list != null) {
                //noinspection ConstantConditions
                list.add(i);
            } else {
                //noinspection ConstantConditions
                field.set(this, i);
            }
        } else if(type.isEnum()) {
            Enum<?> i;
            try {
                i = Enum.valueOf(type.asSubclass(Enum.class), value.trim());
            } catch(IllegalArgumentException e) {
                throw new NumberFormatException(e.getLocalizedMessage());
            }
            assert type.isInstance(i);
            if(list != null) {
                list.add(i);
            } else {
                field.set(this, i);
            }
        } else {
            throw new RuntimeException("Unknown type " + type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows({IllegalAccessException.class})
    public void addElement(ConfigElement element) {
        for(Field field : getClass().getDeclaredFields()) {
            if(field.getType().isInstance(element)) {
                field.set(this, element);
                return;
            }

            if(field.getType().isAssignableFrom(ArrayList.class)) {
                Type type = field.getGenericType();
                if(type instanceof ParameterizedType) {
                    Type[] typeArgs = ((ParameterizedType) type).getActualTypeArguments();
                    assert typeArgs.length == 1;

                    try {
                        Class<?> clazz = Class.forName(typeArgs[0].getTypeName());
                        if(clazz.isInstance(element)) {
                            List coll = (List) field.get(this);
                            if(coll == null) {
                                coll = new ArrayList();
                                field.set(this, coll);
                            }

                            coll.add(element);
                            return;
                        }
                    } catch(ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        otherChildren.put(element.getClass().getSimpleName(), element);
    }
}
