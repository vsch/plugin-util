package com.vladsch.plugin.util.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.actionSystem.Toggleable;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * @author max
 */
public abstract class CheckboxAction extends ToggleAction implements CustomComponentAction {

    protected CheckboxAction() {}

    protected CheckboxAction(final String text) {
        super(text);
    }

    protected CheckboxAction(final String text, final String description, final Icon icon) {
        super(text, description, icon);
    }

    @NotNull
    @Override
    public JComponent createCustomComponent(@NotNull Presentation presentation, @NotNull String place) {
        // this component cannot be stored right here because of action system architecture:
        // one action can be shown on multiple toolbars simultaneously
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);

        checkBox.addActionListener(e -> {
            JCheckBox checkBox1 = (JCheckBox) e.getSource();
            ActionToolbar actionToolbar = UIUtil.getParentOfType(ActionToolbar.class, checkBox1);
            DataContext dataContext = actionToolbar != null ? actionToolbar.getToolbarDataContext() : DataManager.getInstance().getDataContext(checkBox1);
            CheckboxAction.this.actionPerformed(AnActionEvent.createFromAnAction(CheckboxAction.this, null, ActionPlaces.UNKNOWN, dataContext));
        });
        updateCustomComponent(checkBox, presentation);
        return checkBox;
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        super.update(e);
        Presentation presentation = e.getPresentation();
        Object property = presentation.getClientProperty(CUSTOM_COMPONENT_PROPERTY_KEY);
        if (property instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) property;
            updateCustomComponent(checkBox, presentation);
        }
    }

    protected void updateCustomComponent(JCheckBox checkBox, Presentation presentation) {
        checkBox.setText(presentation.getText());
        checkBox.setToolTipText(presentation.getDescription());
        checkBox.setMnemonic(presentation.getMnemonic());
        checkBox.setDisplayedMnemonicIndex(presentation.getDisplayedMnemonicIndex());
        checkBox.setSelected(Toggleable.isSelected(presentation));
        checkBox.setEnabled(presentation.isEnabled());
        checkBox.setVisible(presentation.isVisible());
    }
}
