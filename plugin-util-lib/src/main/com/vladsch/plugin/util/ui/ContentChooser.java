/*
 *
 */
package com.vladsch.plugin.util.ui;

import com.intellij.ide.ui.SplitterProportionsDataImpl;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.SplitterProportionsData;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.JBColor;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.ScrollingUtil;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBList;
import com.intellij.ui.speedSearch.FilteringListModel;
import com.intellij.util.Alarm;
import com.intellij.util.ObjectUtils;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.vladsch.plugin.util.UtilBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ContentChooser<Data> extends DialogWrapper {
    List<Data> myAllContents;
    private Editor myViewer;

    final boolean myUseIdeaEditor;

    final JBList<Item> myList;
    private final JBSplitter mySplitter;
    private final Project myProject;
    private final boolean myAllowMultipleSelections;
    final Alarm myUpdateAlarm;
    private Icon myListEntryIcon;

    public ContentChooser(Project project, String title, Icon icon, boolean useIdeaEditor) {
        this(project, title, icon, useIdeaEditor, false);
    }

    public ContentChooser(Project project, String title, Icon icon, boolean useIdeaEditor, boolean allowMultipleSelections) {
        super(project, true);
        myProject = project;
        myUseIdeaEditor = useIdeaEditor;
        myAllowMultipleSelections = allowMultipleSelections;
        myUpdateAlarm = new Alarm(getDisposable());
        mySplitter = new JBSplitter(true, 0.3f);
        mySplitter.setSplitterProportionKey(getDimensionServiceKey() + ".splitter");
        myList = new JBList<>(new CollectionListModel<>());
        myList.setExpandableItemsEnabled(false);
        myListEntryIcon = icon;

        setOKButtonText(UtilBundle.message("button.ok"));
        setTitle(title);

        init();
    }

    public void setContentIcon(@Nullable Icon icon) {
        myListEntryIcon = icon;
    }

    public void setSplitterOrientation(boolean vertical) {
        mySplitter.setOrientation(vertical);
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myList;
    }

    // add ability to handle extra keys
    protected void listKeyPressed(KeyEvent event) {

    }

    // user deleted an item
    protected void listItemDeleted() {

    }

    @Override
    protected JComponent createCenterPanel() {
        final int selectionMode = myAllowMultipleSelections ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                : ListSelectionModel.SINGLE_SELECTION;
        myList.setSelectionMode(selectionMode);
        if (myUseIdeaEditor) {
            EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
            myList.setFont(scheme.getFont(EditorFontType.PLAIN));
            Color fg = ObjectUtils.chooseNotNull(scheme.getDefaultForeground(), UIUtil.getListForeground());
            Color bg = ObjectUtils.chooseNotNull(scheme.getDefaultBackground(), UIUtil.getListBackground());
            myList.setForeground(fg);
            myList.setBackground(bg);
        }

        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent e) {
                close(OK_EXIT_CODE);
                return true;
            }
        }.installOn(myList);

        myList.setCellRenderer(new MyListCellRenderer());
        myList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    List<Item> list = myList.getSelectedValuesList();

                    // delete should be done in reverse order so as not to affect the index of following deletes
                    int[] indices = new int[list.size()];

                    int iMax = 0;
                    for (Object o : list) {
                        int index = ((Item) o).index;
                        indices[iMax++] = index;
                    }

                    Arrays.sort(indices);

                    int newSelectionIndex = -1;

                    for (int i = iMax; i-- > 0; ) {
                        int index = indices[i];
                        removeContentAt(myAllContents.get(index));
                        if (newSelectionIndex < 0) {
                            newSelectionIndex = index;
                        }
                    }

                    rebuildListContent();
                    if (myAllContents.isEmpty()) {
                        close(CANCEL_EXIT_CODE);
                        return;
                    }
                    newSelectionIndex = Math.min(newSelectionIndex, myAllContents.size() - 1);
                    myList.setSelectedIndex(newSelectionIndex);
                    listItemDeleted();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doOKAction();
                } else {
                    final char aChar = e.getKeyChar();
                    if (aChar >= '0' && aChar <= '9') {
                        int idx = aChar == '0' ? 9 : aChar - '1';
                        if (idx < myAllContents.size()) {
                            myList.setSelectedIndex(idx);
                            e.consume();
                            doOKAction();
                        }
                    } else {
                        listKeyPressed(e);
                    }
                }
            }
        });

        final JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(myList);

        // needed for 2016.3 since speed search wrapper is not compatible with 2019.1
        try {
            final JComponent wrappedList = ListWithFilter.wrap(myList, scrollPane, o -> o.longText);
            mySplitter.setFirstComponent(wrappedList);
        } catch (Throwable ignored) {
            mySplitter.setFirstComponent(scrollPane);
        }
        mySplitter.setSecondComponent(new JPanel());
        rebuildListContent();

        ScrollingUtil.installActions(myList);
        ScrollingUtil.ensureSelectionExists(myList);
        updateViewerForSelection();
        myList.addListSelectionListener(e -> {
            myUpdateAlarm.cancelAllRequests();
            myUpdateAlarm.addRequest(this::updateViewerForSelection, 100);
        });

        mySplitter.setPreferredSize(JBUI.size(500, 500));

        SplitterProportionsData d = new SplitterProportionsDataImpl();
        d.externalizeToDimensionService(getClass().getName());
        d.restoreSplitterProportions(mySplitter);

        return mySplitter;
    }

    protected abstract void removeContentAt(final Data content);

    public void updateListContents(final boolean focusList) {
        rebuildListContent();
        if (myAllContents.isEmpty()) {
            close(CANCEL_EXIT_CODE);
        }
        if (focusList) {
            myList.requestFocus();
        }
    }

    @Override
    protected String getDimensionServiceKey() {
        return getClass().getName(); // store different values for multi-paste, history and commit messages
    }

    @Override
    protected void doOKAction() {
        if (getSelectedIndex() < 0) return;
        super.doOKAction();
    }

    @SuppressWarnings("WeakerAccess")
    protected void updateViewerForSelection(@NotNull Editor editor, @NotNull List<Data> allContents, @NotNull int[] selectedIndices) {

    }

    @Nullable
    protected JComponent getAboveEditorComponent() {
        return null;
    }

    void updateViewerForSelection() {
        if (myAllContents.isEmpty()) return;
        String fullString = getSelectedText();

        if (myViewer != null) {
            EditorFactory.getInstance().releaseEditor(myViewer);
        }

        if (myUseIdeaEditor) {
            myViewer = createIdeaEditor(fullString);
            JComponent component = myViewer.getComponent();
            JComponent aboveEditorComponent = getAboveEditorComponent();
            component.setPreferredSize(JBUI.size(300, 500));
            if (aboveEditorComponent != null) {
                JPanel panel = JBUI.Panels.simplePanel(0, 10)
                        .addToTop(aboveEditorComponent)
                        .addToCenter(component);

                mySplitter.setSecondComponent(panel);
            } else {
                mySplitter.setSecondComponent(component);
            }
            updateViewerForSelection(myViewer, myAllContents, getSelectedIndices());
        } else {
            final JTextArea textArea = new JTextArea(fullString);
            textArea.setRows(3);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setSelectionStart(0);
            textArea.setSelectionEnd(textArea.getText().length());
            textArea.setEditable(false);
            mySplitter.setSecondComponent(ScrollPaneFactory.createScrollPane(textArea));
        }
        mySplitter.revalidate();
    }

    protected Editor createIdeaEditor(String text) {
        Document doc = EditorFactory.getInstance().createDocument(text);
        Editor editor = EditorFactory.getInstance().createViewer(doc, myProject);
        editor.getSettings().setFoldingOutlineShown(false);
        editor.getSettings().setLineNumbersShown(false);
        editor.getSettings().setLineMarkerAreaShown(false);
        editor.getSettings().setIndentGuidesShown(false);
        return editor;
    }

    @Override
    public void dispose() {
        super.dispose();

        SplitterProportionsData d = new SplitterProportionsDataImpl();
        d.externalizeToDimensionService(getClass().getName());
        d.saveSplitterProportions(mySplitter);

        if (myViewer != null) {
            EditorFactory.getInstance().releaseEditor(myViewer);
            myViewer = null;
        }
    }

    @SuppressWarnings("WeakerAccess")
    @NotNull
    protected String getShortStringFor(Data content, String fullString) {
        String shortString;
        int newLineIdx = fullString.indexOf('\n');
        if (newLineIdx == -1) {
            shortString = fullString.trim();
        } else {
            final String tooLongSuffix = getShortStringTooLongSuffix(content);
            int lastLooked = 0;
            do {
                int nextLineIdx = fullString.indexOf("\n", lastLooked);
                if (nextLineIdx > lastLooked) {
                    shortString = fullString.substring(lastLooked, nextLineIdx).trim() + tooLongSuffix;
                    break;
                } else if (nextLineIdx == -1) {
                    shortString = tooLongSuffix;
                    break;
                }
                lastLooked = nextLineIdx + 1;
            } while (true);
        }
        return shortString;
    }

    @NotNull
    protected String getShortStringTooLongSuffix(Data content) {
        return " ...";
    }

    void rebuildListContent() {
        ArrayList<Item> items = new ArrayList<>();
        int i = 0;
        List<Data> contents = new ArrayList<>(getContents());
        for (Data content : contents) {
            String fullString = getStringRepresentationFor(content);
            if (fullString != null) {
                fullString = StringUtil.convertLineSeparators(fullString);
                String shortString = getShortStringFor(content, fullString);
                items.add(new Item(i++, shortString, fullString));
            }
        }
        myAllContents = contents;
        ListModel<Item> listModel = myList.getModel();
        if (listModel instanceof FilteringListModel) {
            FilteringListModel<Item> filteringListModel = (FilteringListModel<Item>) listModel;
            ((CollectionListModel<Item>) filteringListModel.getOriginalModel()).removeAll();
            filteringListModel.addAll(items);
            ListWithFilter<?> listWithFilter = UIUtil.getParentOfType(ListWithFilter.class, myList);
            if (listWithFilter != null) {
                listWithFilter.getSpeedSearch().update();
                if (filteringListModel.getSize() == 0) listWithFilter.resetFilter();
            }
        } else if (listModel instanceof CollectionListModel) {
            // DEPRECATED: needed for 2016.3 since speed search wrapper is not compatible with 2019.1
            ((CollectionListModel<Item>) listModel).removeAll();
            ((CollectionListModel<Item>) listModel).add(items);
        }
    }

    protected abstract String getStringRepresentationFor(final Data content);

    protected abstract List<Data> getContents();

    public int getSelectedIndex() {
        Item item = myList.getSelectedValue();
        return item == null ? -1 : item.index;
    }

    public void setSelectedIndex(int index) {
        myList.setSelectedIndex(index);
        ScrollingUtil.ensureIndexIsVisible(myList, index, 0);
        updateViewerForSelection();
    }

    public void setSelectedIndices(int[] indices) {
        myList.setSelectedIndices(indices);
    }

    @NotNull
    public int[] getSelectedIndices() {
        List<Item> values = myList.getSelectedValuesList();
        int iMax = values.size();
        int[] result = new int[iMax];
        for (int i = 0; i < iMax; i++) {
            result[i] = values.get(i).index;
        }
        return result;
    }

    public List<Data> getAllContents() {
        return myAllContents;
    }

    @SuppressWarnings("WeakerAccess")
    protected Icon getListEntryIcon(@NotNull Data data) {
        return myListEntryIcon;
    }

    @NotNull
    public String getSelectedText() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object o : myList.getSelectedValuesList()) {
            if (first) first = false;
            else sb.append("\n");
            String s = ((Item) o).longText;
            sb.append(StringUtil.convertLineSeparators(s));
        }
        return sb.toString();
    }

    private class MyListCellRenderer extends ColoredListCellRenderer<Item> {
        MyListCellRenderer() {}

        @Override
        protected void customizeCellRenderer(@NotNull JList<? extends Item> list, Item value, int index, boolean selected, boolean hasFocus) {
            setIcon(getListEntryIcon(myAllContents.get(index)));
            if (myUseIdeaEditor) {
                int max = list.getModel().getSize();
                String indexString = String.valueOf(index + 1);
                int count = String.valueOf(max).length() - indexString.length();
                char[] spaces = new char[count];
                Arrays.fill(spaces, ' ');
                String prefix = indexString + new String(spaces) + "  ";
                append(prefix, SimpleTextAttributes.GRAYED_ATTRIBUTES);
//            } else if (UIUtil.isUnderGTKLookAndFeel()) {
//                // Fix GTK background
//                Color background = selected ? UIUtil.getListSelectionBackground() : UIUtil.getListBackground();
//                UIUtil.changeBackGround(this, background);
            }
            String text = value.shortText;

            FontMetrics metrics = list.getFontMetrics(list.getFont());
            int charWidth = metrics.charWidth('m');
            int maxLength = list.getParent().getParent().getWidth() * 3 / charWidth / 2;
            text = StringUtil.first(text, maxLength, true); // do not paint long strings
            append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }

    private static class Item {
        final int index;
        final String shortText;
        final String longText;

        Item(int index, String shortText, String longText) {
            this.index = index;
            this.shortText = shortText;
            this.longText = longText;
        }
    }
}
