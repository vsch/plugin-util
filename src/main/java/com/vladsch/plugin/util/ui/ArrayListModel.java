// Copyright (c) 2015-2020 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
//
// This code is private property of the copyright holder and cannot be used without
// having obtained a license or prior written permission of the copyright holder.
//

package com.vladsch.plugin.util.ui;

import javax.swing.AbstractListModel;
import java.util.ArrayList;
import java.util.Collection;

public class ArrayListModel<E> extends AbstractListModel<E> {
    private final ArrayList<E> myList;

    public ArrayListModel(final Collection<E> list) {
        myList = new ArrayList<>(list);
    }

    public ArrayListModel() {
        myList = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return myList.size();
    }

    @Override
    public E getElementAt(final int index) {
        return myList.get(index);
    }

    public void clear() {
        int size = myList.size();
        myList.clear();
        this.fireIntervalRemoved(this, 0, size);
    }

    public void add(final E value) {
        int size = myList.size();
        myList.add(value);
        this.fireIntervalAdded(this, size, size + 1);
    }
}
