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
