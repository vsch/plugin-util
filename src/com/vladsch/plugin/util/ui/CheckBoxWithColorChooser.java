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

import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.ClickListener;
import com.intellij.ui.ColorChooser;
import com.intellij.ui.ColorUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Vladimir Schneider
 * Added Enabled property propagation to checkbox and repainting of color tab on color change
 * Added unselected color property to display when checkbox is not selected
 * Added update runnable to callback on color change
 * @author Konstantin Bulenkov
 */
public class CheckBoxWithColorChooser extends JPanel {
    Color myColor;
    final JCheckBox myCheckbox;
    @Nullable Color myUnselectedColor;
    Runnable myUpdateRunnable;
    private final JButton myColorButton;

    public CheckBoxWithColorChooser(String text, boolean selected, @NotNull Color color) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        myColor = color;
        myUnselectedColor = null;
        myCheckbox = new JCheckBox(text, selected);

        add(myCheckbox);
        myColorButton = new MyColorButton();
        add(myColorButton);
        myUpdateRunnable = null;

        myCheckbox.addActionListener((event) -> {
            myColorButton.repaint();
            if (myUpdateRunnable != null) {
                myUpdateRunnable.run();
            }
        });
    }

    public CheckBoxWithColorChooser(String text, boolean selected) {
        this(text, selected, Color.WHITE);
    }

    public CheckBoxWithColorChooser(String text) {
        this(text, false);
    }

    @Nullable
    public Color getUnselectedColor() {
        return myUnselectedColor;
    }

    public void setUnselectedColor(@Nullable final Color unselectedColor) {
        myUnselectedColor = unselectedColor;
        if (!myCheckbox.isSelected()) {
            myColorButton.repaint();
        }
    }

    public Runnable getUpdateRunnable() {
        return myUpdateRunnable;
    }

    public void setUpdateRunnable(final Runnable updateRunnable) {
        myUpdateRunnable = updateRunnable;
    }

    public void setMnemonic(char c) {
        myCheckbox.setMnemonic(c);
    }

    public Color getColor() {
        return myColor;
    }

    public void setColor(Color color) {
        myColor = color;
        myColorButton.repaint();
        if (myUpdateRunnable != null) {
            myUpdateRunnable.run();
        }
    }

    public void setSelected(boolean selected) {
        myCheckbox.setSelected(selected);
        myColorButton.repaint();
    }

    public boolean isSelected() {
        return myCheckbox.isSelected();
    }

    public void setEnabled(boolean enabled) {
        myCheckbox.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return myCheckbox.isEnabled();
    }

    static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    static final int LEFT_MARGIN = 5;
    static final int CORNER_RADIUS = 6;

    private class MyColorButton extends JButton {
        @NotNull
        @Override
        public Cursor getCursor() {
            return myCheckbox.isSelected() && myCheckbox.isEnabled() ? handCursor : super.getCursor();
        }

        MyColorButton() {
            setMargin(new Insets(0, 0, 0, 5));
            setDefaultCapable(false);
            setFocusable(false);
            if (SystemInfo.isMac) {
                putClientProperty("JButton.buttonType", "square");
            }
            new ClickListener() {
                @Override
                public boolean onClick(@NotNull MouseEvent e, int clickCount) {
                    if (myCheckbox.isSelected()) {
                        final Color color = ColorChooser.chooseColor(myCheckbox, "Chose Color", CheckBoxWithColorChooser.this.myColor);
                        if (color != null) {
                            myColor = color;
                            MyColorButton.this.repaint();
                            if (myUpdateRunnable != null) {
                                myUpdateRunnable.run();
                            }
                        }
                    }
                    return true;
                }
            }.installOn(this);
        }

        @Override
        public void paint(Graphics g) {
            final Color color = g.getColor();
            int width = getWidth() - LEFT_MARGIN;
            int height = getHeight();
            if (UIUtil.isJreHiDPI()) {
                final Graphics2D g2d = (Graphics2D) g.create(LEFT_MARGIN, 0, width, height);
                final GraphicsConfig graphicsConfig = new GraphicsConfig(g2d);
                graphicsConfig.setAntialiasing(true);
                float s = JBUI.sysScale();
                g2d.scale(1 / s, 1 / s);

                int iSize = CORNER_RADIUS;
                g2d.setColor(myCheckbox.isSelected() || myUnselectedColor == null ? myColor : myUnselectedColor);
                RoundRectangle2D.Double shape = new RoundRectangle2D.Double(0, 0, (width - 1) * s, (height - 1) * s, iSize * s, iSize * s);
                g2d.fill(shape);
                g2d.setColor(ColorUtil.withAlpha(Color.BLACK, .40));
                g2d.draw(shape);

                graphicsConfig.restore();
                g2d.scale(1, 1);
                g2d.dispose();
            } else {
                g.setColor(JBColor.GRAY);
                g.fillRoundRect(LEFT_MARGIN, 0, width, height, CORNER_RADIUS, CORNER_RADIUS);
                g.setColor(myCheckbox.isSelected() || myUnselectedColor == null ? myColor : myUnselectedColor);
                g.fillRoundRect(LEFT_MARGIN + 1, 1, width - 2, height - 2, CORNER_RADIUS - 1, CORNER_RADIUS - 1);
                g.setColor(color);
            }
            g.setColor(color);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            int size = 14;
            return new Dimension(JBUI.scale(size + LEFT_MARGIN), JBUI.scale(size));
        }
    }
}
