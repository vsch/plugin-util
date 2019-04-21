package com.vladsch.plugin.util.ui;

import com.intellij.icons.AllIcons;
import com.intellij.ide.ui.UISettings;
import com.intellij.notification.impl.ui.NotificationsUtil;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

public class TextIcon implements Icon {
    private final String myStr;
    private final JComponent myComponent;
    private final Color myTextColor;
    private final int myWidth;
    private final Font myFont;

    public TextIcon(JComponent component, @NotNull String str, @NotNull Color textColor) {
        myStr = str;
        myComponent = component;
        myTextColor = textColor;
        myFont = new Font(NotificationsUtil.getFontName(), Font.BOLD, JBUI.scale(9));
        myWidth = myComponent.getFontMetrics(myFont).stringWidth(myStr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextIcon)) return false;

        TextIcon icon = (TextIcon) o;

        if (myWidth != icon.myWidth) return false;
        if (!myComponent.equals(icon.myComponent)) return false;
        if (!myStr.equals(icon.myStr)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = myStr.hashCode();
        result = 31 * result + myComponent.hashCode();
        result = 31 * result + myWidth;
        return result;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        UISettings.setupAntialiasing(g);

        Font originalFont = g.getFont();
        Color originalColor = g.getColor();
        g.setFont(myFont);

        x += (getIconWidth() - myWidth) / 2;
        y += SimpleColoredComponent.getTextBaseLine(g.getFontMetrics(), getIconHeight());

        int length = myStr.length();
        if (SystemInfo.isMac || (SystemInfo.isWindows && length == 2)) {
            x += JBUI.scale(1);
        }

        g.setColor(myTextColor);
        g.drawString(myStr.substring(0, 1), x, y);

        if (length == 2) {
            x += g.getFontMetrics().charWidth(myStr.charAt(0)) - JBUI.scale(1);
            g.drawString(myStr.substring(1), x, y);
        }

        g.setFont(originalFont);
        g.setColor(originalColor);
    }

    @Override
    public int getIconWidth() {
        return AllIcons.Ide.Notification.NoEvents.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return AllIcons.Ide.Notification.NoEvents.getIconHeight();
    }
}
