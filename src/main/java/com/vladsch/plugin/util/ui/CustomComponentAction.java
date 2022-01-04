package com.vladsch.plugin.util.ui;

import com.intellij.openapi.util.Key;

public interface CustomComponentAction extends com.intellij.openapi.actionSystem.ex.CustomComponentAction {
    Key<Object> CUSTOM_COMPONENT_PROPERTY_KEY = new Key<>("customComponent");
}
