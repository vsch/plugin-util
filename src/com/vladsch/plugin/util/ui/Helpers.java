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

import com.intellij.ide.ui.AntialiasingType;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.UIUtil;
import com.vladsch.flexmark.util.ui.HtmlBuilder;
import com.vladsch.flexmark.util.ui.HtmlHelpers;

import javax.swing.JComponent;
import java.awt.Component;

@SuppressWarnings("WeakerAccess")
public class Helpers {
    public static void initAntiAliasing(Component main) {
        for (JComponent c : UIUtil.uiTraverser(main).filter(JComponent.class)) {
            GraphicsUtil.setAntialiasingType(c, AntialiasingType.getAAHintForSwingComponent());
        }
    }

    public static java.awt.Color errorColor() {
        TextAttributes attribute = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(CodeInsightColors.ERRORS_ATTRIBUTES);
        java.awt.Color color = JBColor.RED;
        if (attribute != null) {
            if (attribute.getForegroundColor() != null) {
                color = attribute.getForegroundColor();
            } else if (attribute.getEffectColor() != null) {
                color = attribute.getEffectColor();
            } else if (attribute.getErrorStripeColor() != null) {
                color = attribute.getErrorStripeColor();
            }
        }
        return color;
    }

    public static java.awt.Color warningColor() {
        TextAttributes attribute = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(CodeInsightColors.WARNINGS_ATTRIBUTES);
        java.awt.Color color = JBColor.ORANGE;
        if (attribute != null) {
            if (attribute.getForegroundColor() != null) {
                color = attribute.getForegroundColor();
            } else if (attribute.getEffectColor() != null) {
                color = attribute.getEffectColor();
            } else if (attribute.getErrorStripeColor() != null) {
                color = attribute.getErrorStripeColor();
            }
        }
        return color;
    }

    public static void setEnabledOfChildren(JComponent parent, boolean isEnabled) {
        int iMax = parent.getComponentCount();

        for (int i = 0; i < iMax; i++) {
            Component component = parent.getComponent(i);
            component.setEnabled(isEnabled);
            if (component instanceof JComponent) {
                setEnabledOfChildren((JComponent) component, isEnabled);
            }
        }
    }

    static {
        HtmlBuilder.addColorStylerClass(JBColor.class);
    }
    public static java.awt.Color errorColor(java.awt.Color color) {
        return HtmlHelpers.mixedColor(color, errorColor());
    }

    public static java.awt.Color warningColor(java.awt.Color color) {
        return HtmlHelpers.mixedColor(color, warningColor());
    }
}
