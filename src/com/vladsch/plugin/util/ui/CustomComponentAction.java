package com.vladsch.plugin.util.ui;

import com.intellij.openapi.util.Key;

import javax.swing.JComponent;

public interface CustomComponentAction extends com.intellij.openapi.actionSystem.ex.CustomComponentAction {
    Key<JComponent> CUSTOM_COMPONENT_PROPERTY_KEY = new Key<>("customComponent");
}
