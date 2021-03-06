package com.vladsch.plugin.util.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.actionSystem.ShortcutSet;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.KeyStroke;

public class CommonUIShortcuts {
    public static final String ACTION_MOVE_LINE_UP_ACTION = "MoveLineUp";
    public static final String ACTION_MOVE_LINE_DOWN_ACTION = "MoveLineDown";

    public static class ShortcutSets {
        public static final ShortcutSet ALT_ENTER = CommonShortcuts.ALT_ENTER;
        public static final ShortcutSet CTRL_ENTER = CommonShortcuts.CTRL_ENTER;
        public static final ShortcutSet DOUBLE_CLICK_1 = CommonShortcuts.DOUBLE_CLICK_1;
        public static final ShortcutSet ENTER = CommonShortcuts.ENTER;
        public static final ShortcutSet ESCAPE = CommonShortcuts.ESCAPE;
        public static final ShortcutSet INSERT = CommonShortcuts.INSERT;
        public static final ShortcutSet MOVE_DOWN = CommonShortcuts.MOVE_DOWN;
        public static final ShortcutSet MOVE_UP = CommonShortcuts.MOVE_UP;
    }

    // @formatter:off
    public static KeyStroke getInsertKeystroke() { return CommonShortcuts.getInsertKeystroke(); }
    public static ShortcutSet getCloseActiveWindow() { return CommonShortcuts.getCloseActiveWindow(); }
    public static ShortcutSet getContextHelp() { return CommonShortcuts.getContextHelp(); }
    public static ShortcutSet getCopy() { return CommonShortcuts.getCopy(); }
    public static ShortcutSet getDelete() { return CommonShortcuts.getDelete(); }
    public static ShortcutSet getDiff() { return CommonShortcuts.getDiff(); }
    public static ShortcutSet getDuplicate() { return CommonShortcuts.getDuplicate(); }
    public static ShortcutSet getEditSource() { return CommonShortcuts.getEditSource(); }
    public static ShortcutSet getFind() { return CommonShortcuts.getFind(); }
    public static ShortcutSet getMove() { return CommonShortcuts.getMove(); }
    public static ShortcutSet getMoveDown() { return CommonShortcuts.getMoveDown(); }
    public static ShortcutSet getMoveEnd() { return CommonShortcuts.getMoveEnd(); }
    public static ShortcutSet getMoveHome() { return CommonShortcuts.getMoveHome(); }
    public static ShortcutSet getMovePageDown() { return CommonShortcuts.getMovePageDown(); }
    public static ShortcutSet getMovePageUp() { return CommonShortcuts.getMovePageUp(); }
    public static ShortcutSet getMoveUp() { return CommonShortcuts.getMoveUp(); }
    public static ShortcutSet getNew() { return CommonShortcuts.getNew(); }
    public static ShortcutSet getNewForDialogs() { return CommonShortcuts.getNewForDialogs(); }
    public static ShortcutSet getPaste() { return CommonShortcuts.getPaste(); }
    public static ShortcutSet getRecentFiles() { return CommonShortcuts.getRecentFiles(); }
    public static ShortcutSet getRename() { return CommonShortcuts.getRename(); }
    public static ShortcutSet getRerun() { return CommonShortcuts.getRerun(); }
    public static ShortcutSet getViewSource() { return CommonShortcuts.getViewSource(); }

    public static ShortcutSet getMoveLineUp() { return shortcutsById("MoveLineUp"); }
    public static ShortcutSet getMoveLineDown() { return shortcutsById("MoveLineDown"); }
    public static ShortcutSet getMultiplePaste() { return shortcutsById("PasteMultiple"); }

// @formatter:on

    @NotNull
    private static CustomShortcutSet shortcutsById(String actionId) {
        KeymapManager keymapManager = KeymapManager.getInstance();
        if (keymapManager == null) {
            return new CustomShortcutSet(Shortcut.EMPTY_ARRAY);
        }
        return new CustomShortcutSet(keymapManager.getActiveKeymap().getShortcuts(actionId));
    }

    @NotNull
    public static CustomShortcutSet shortcutsFrom(ShortcutSet... sets) {
        int count = 0;
        for (ShortcutSet set : sets) {
            count += set.getShortcuts().length;
        }
        Shortcut[] shortcuts = new Shortcut[count];
        count = 0;
        for (ShortcutSet set : sets) {
            int length = set.getShortcuts().length;
            System.arraycopy(set.getShortcuts(), 0, shortcuts, count, length);
            count += length;
        }
        return new CustomShortcutSet(shortcuts);
    }

    @NotNull
    public static String getNthShortcutText(@NotNull AnAction action, int n) {
        Shortcut[] shortcuts = action.getShortcutSet().getShortcuts();
        String shortcutText = "";
        for (Shortcut shortcut : shortcuts) {
            if (shortcut instanceof KeyboardShortcut) {
                String text = KeymapUtil.getShortcutText(shortcut);
                if (!text.isEmpty()) {
                    shortcutText = text;
                    if (--n <= 0) break;
                }
            }
        }
        return shortcutText;
    }
}
