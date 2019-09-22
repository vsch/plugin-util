package com.vladsch.plugin.util.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.JComponent;

public abstract class DialogWrapperParams<T> extends DialogWrapper {
    protected final T myParams;

    public DialogWrapperParams(JComponent parent, boolean canBeParent, final T params) {
        super(parent, canBeParent);

        this.myParams = params;
    }

    public T getParams() {
        return myParams;
    }
}
