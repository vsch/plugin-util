/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package com.vladsch.plugin.util.ui;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DynamicListAdaptable<A extends DynamicListAdaptable<A>> implements ComboBoxAdaptable<DynamicListAdaptable<A>> {
    public final int intValue;
    public final @NotNull String displayName;

    protected DynamicListAdaptable(final int intValue, @NotNull final String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    protected interface Factory<F> {
        F create(int intValue, @NotNull String displayName);
    }

    @NotNull
    public static List<String> getDisplayNames(@NotNull DynamicListAdaptable[] values) {
        ArrayList<String> list = new ArrayList<>(values.length);
        for (DynamicListAdaptable item : values) {
            list.add(item.displayName);
        }
        return list;
    }

    @NotNull
    protected static List<String> asList(@NotNull String[] valueList) {
        ArrayList<String> list = new ArrayList<>(valueList.length);
        Collections.addAll(list, valueList);
        return list;
    }

    protected static <V extends DynamicListAdaptable<V>> V[] updateValues(@NotNull V empty, @NotNull Iterable<String> valueList, final boolean addEmpty, final @NotNull Factory<V> factory) {
        int iMax = 0;

        for (String item : valueList) iMax++;

        //noinspection unchecked
        V[] values = (V[]) Array.newInstance(empty.getClass(), iMax + (addEmpty ? 1 : 0));

        int i = 0;
        if (addEmpty) {
            values[i] = empty;
            i++;
        }

        for (String value : valueList) {
            values[i] = factory.create(i, value);
            i++;
        }
        return values;
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    @Override
    public String name() {
        return displayName;
    }

    @Override
    public int getIntValue() {
        return intValue;
    }
}
