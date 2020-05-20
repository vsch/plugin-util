package com.vladsch.plugin.util.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

@SuppressWarnings("WeakerAccess")
public abstract class TextFieldAction extends AnAction implements CustomComponentAction {
    public static final String TEXT_FIELD_ORIGINAL = "textFieldOriginal";
    public static final String TEXT_FIELD_BACKGROUND = "textFieldBackground";

    protected TextFieldAction() {}

    protected TextFieldAction(final String text) {
        super(text);
    }

    protected TextFieldAction(final String text, final String description, final Icon icon) {
        super(text, description, icon);
    }

    protected void updateOnFocusLost(String text, Presentation presentation) {

    }

    protected void updateOnFocusGained(String text, Presentation presentation) {

    }

    protected void updateOnTextChange(String text, Presentation presentation) {

    }

    @NotNull
    @Override
    public JComponent createCustomComponent(@NotNull final Presentation presentation) {
        // this component cannot be stored right here because of action system architecture:
        // one action can be shown on multiple toolbars simultaneously
        final JTextField textField = new JTextField();
        textField.setOpaque(true);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {
                presentation.putClientProperty(TEXT_FIELD_ORIGINAL, textField.getText());
                updateOnFocusGained(textField.getText(), presentation);
            }

            @Override
            public void focusLost(final FocusEvent e) {
                updateOnFocusLost(textField.getText(), presentation);
            }
        });

        textField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull final DocumentEvent e) {
                String text = textField.getText();
                if (!text.equals(presentation.getText())) {
                    presentation.setText(text);
                    updateOnTextChange(textField.getText(), presentation);
                }
            }
        });

        updateCustomComponent(textField, presentation);
        return textField;
    }

    protected void doAction(final FocusEvent e) {
        JTextField textField = (JTextField) e.getSource();
        ActionToolbar actionToolbar = UIUtil.getParentOfType(ActionToolbar.class, textField);
        DataContext dataContext =
                actionToolbar != null ? actionToolbar.getToolbarDataContext() : DataManager.getInstance().getDataContext(textField);
        TextFieldAction.this.actionPerformed(AnActionEvent.createFromAnAction(TextFieldAction.this, null, ActionPlaces.UNKNOWN, dataContext));
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        super.update(e);
        Presentation presentation = e.getPresentation();
        Object property = presentation.getClientProperty(CustomComponentAction.CUSTOM_COMPONENT_PROPERTY);
        if (property instanceof JTextField) {
            JTextField textField = (JTextField) property;
            updateCustomComponent(textField, presentation);
        }
    }

    private void updateCustomComponent(JTextField textField, Presentation presentation) {
        String text = presentation.getText();
        if (!textField.getText().equals(text)) {
            ApplicationManager.getApplication().invokeLater(() -> {
                textField.setText(text == null ? "" : text);
            });
        }

        textField.setToolTipText(presentation.getDescription());

        Object background = presentation.getClientProperty(TEXT_FIELD_BACKGROUND);
        if (background instanceof Color) {
            textField.setBackground((Color) background);
        }
        textField.setEnabled(false);
        textField.setVisible(presentation.isVisible());
    }
}
