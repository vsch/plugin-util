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

import com.intellij.openapi.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class OnIt {
    final private ArrayList<Pair<ComboBoxAdaptable, Runnable>> myList = new ArrayList<>();

    public OnIt to(ComboBoxAdaptable type, Runnable doRun) {
        myList.add(Pair.create(type, doRun));
        return this;
    }

    public List<Pair<ComboBoxAdaptable, Runnable>> getList() {
        return myList;
    }
}
