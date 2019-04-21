package com.vladsch.plugin.util.ui;

import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.JComponent;

public abstract class DialogWrapperParams<T> extends DialogWrapper {
    protected final T mySettings;

    public DialogWrapperParams(JComponent parent, boolean canBeParent, final T settings) {
        super(parent, canBeParent);

        this.mySettings = settings;
    }

    public T getSettings() {
        return mySettings;
    }
}
