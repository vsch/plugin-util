package com.vladsch.plugin.util.ui;

import com.intellij.ide.ui.AntialiasingType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.IconLoader.CachedImageIcon;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.GraphicsUtil;
import com.intellij.util.ui.UIUtil;
import com.vladsch.flexmark.util.misc.ImageUtils;
import com.vladsch.flexmark.util.html.ui.HtmlHelpers;
import com.vladsch.plugin.util.AppUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.intellij.openapi.diagnostic.Logger.getInstance;

@SuppressWarnings("WeakerAccess")
public class Helpers {
    private static final Logger LOG = getInstance("com.vladsch.plugin.util.ui.icons");

    private static boolean ourLoadPng = !AppUtils.isSvgLoadIconAvailable();
    private static boolean ourTestSvg = false;

    public static boolean isSvgIcons() {
        return !ourLoadPng;
    }

    public static Icon load(@NotNull String path, @NotNull Class clazz) {
        //return IconLoader.getIcon(path, PluginIcons.class);
        Icon icon = null;

        if (!(ourLoadPng && path.endsWith(".svg"))) {
            try {
                icon = IconLoader.getIcon(path, clazz);

                if (ourTestSvg && path.endsWith(".svg")) {
                    // test the first one
                    ourTestSvg = false;

                    if (icon.getIconWidth() < 16) {
                        ourLoadPng = true;
                    } else {
                        // could be 2017 which loads SVG as Black and White
                        ourLoadPng = true;
                        if (icon instanceof CachedImageIcon) {
                            CachedImageIcon cachedImageIcon = (CachedImageIcon) icon;
                            try {
                                Method m = cachedImageIcon.getClass().getDeclaredMethod("getRealIcon");
                                m.setAccessible(true);
                                ImageIcon imageIcon = (ImageIcon) m.invoke(cachedImageIcon);
                                if (imageIcon != null) {
                                    ourLoadPng = isBlackAndWhite(imageIcon);
                                }
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {

                            }
                        }
                    }

                    if (ourLoadPng) icon = null;
                }
            } catch (Throwable t) {
                LOG.error(t);
                icon = null;
            }
        }

        if (icon == null) {
            // cannot load SVG, we'll load PNG
            // diagnostic/2876
            String modPath = !path.endsWith(".svg") ? path : path.substring(0, path.length() - ".svg".length()).replace("/svg/", "/png/") + ".png";
            icon = IconLoader.getIcon(modPath, clazz);
        }
        return icon;
    }

    public static boolean isBlackAndWhite(final ImageIcon imageIcon) {
        BufferedImage image = ImageUtils.toBufferedImage(imageIcon.getImage());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                try {
                    int rgb = image.getRGB(x, y);
                    if ((rgb & 0x00ffffff) != 0) {
                        return false;
                    }
                } catch (Throwable ignored) {
                    // happens on SVG icons
                    return false;
                }
            }
        }
        return true;
    }

    public static void initAntiAliasing(Component main) {
        for (JComponent c : UIUtil.uiTraverser(main).filter(JComponent.class)) {
            GraphicsUtil.setAntialiasingType(c, AntialiasingType.getAAHintForSwingComponent());
        }
    }

    public static java.awt.Color errorColor() {
        TextAttributes attribute = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(CodeInsightColors.ERRORS_ATTRIBUTES);
        java.awt.Color color = new JBColor(new Color(0xDE242F), JBColor.RED);
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

    public static java.awt.Color unusedColor() {
        TextAttributes attribute = EditorColorsManager.getInstance().getGlobalScheme().getAttributes(CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
        java.awt.Color color = JBColor.GRAY;
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

    public static java.awt.Color errorColor(java.awt.Color color) {
        return HtmlHelpers.mixedColor(color, errorColor());
    }

    public static java.awt.Color warningColor(java.awt.Color color) {
        return HtmlHelpers.mixedColor(color, warningColor());
    }

    public static int mousePositionVirtualSpaces(@NotNull Editor editor) {
        if (editor.getSettings().isVirtualSpace()) {
            JComponent editorComponent = editor.getContentComponent();
            Point mouseScreenPos = MouseInfo.getPointerInfo().getLocation();
            Point editorScreenPos = editorComponent.getLocationOnScreen();
            Point mouseEditorPos = new Point(mouseScreenPos.x - editorScreenPos.x, mouseScreenPos.y - editorScreenPos.y);
            if (mouseEditorPos.x >= 0 && mouseEditorPos.x < editorComponent.getWidth()
                    && mouseEditorPos.y >= 0 && mouseEditorPos.y < editorComponent.getHeight()) {
                LogicalPosition mouseLogicalPos = editor.xyToLogicalPosition(mouseEditorPos);
                LogicalPosition caretOffsetLogicalPos = editor.getCaretModel().getPrimaryCaret().getLogicalPosition();

                if (caretOffsetLogicalPos.column < mouseLogicalPos.column) {
                    // insert spaces to compensate
                    return mouseLogicalPos.column - caretOffsetLogicalPos.column;
                }
            }
        }
        return 0;
    }
}
