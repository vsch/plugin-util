/*
 *
 */
package com.vladsch.plugin.util.ui;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.util.PopupUtil;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.LightColors;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.UIBundle;
import com.intellij.ui.speedSearch.NameFilteringListModel;
import com.intellij.ui.speedSearch.SpeedSearch;
import com.intellij.ui.speedSearch.SpeedSearchSupply;
import com.intellij.util.Function;
import com.intellij.util.ui.ComponentWithEmptyText;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;

public class ListWithFilter<T> extends JPanel implements DataProvider {
    final JList<T> myList;
    final SearchTextField mySearchField = new SearchTextField(false);
    private final NameFilteringListModel<T> myModel;
    private final JScrollPane myScrollPane;
    final MySpeedSearch mySpeedSearch;
    boolean myAutoPackHeight = true;

    @Override
    public Object getData(@NotNull @NonNls String dataId) {
        if (SpeedSearchSupply.SPEED_SEARCH_CURRENT_QUERY.is(dataId)) {
            return mySearchField.getText();
        }
        return null;
    }

    @NotNull
    public static <T> ListWithFilter<T> wrap(@NotNull JList<T> list, @NotNull JScrollPane scrollPane, @Nullable Function<? super T, String> namer) {
        return wrap(list, scrollPane, namer, false);
    }

    @NotNull
    public static <T> ListWithFilter<T> wrap(
            @NotNull JList<T> list, @NotNull JScrollPane scrollPane, @Nullable Function<? super T, String> namer,
            boolean highlightAllOccurrences
    ) {
        return new ListWithFilter<>(list, scrollPane, namer, highlightAllOccurrences);
    }

    // DEPRECATED: replacement appeared in 2019-03-04
    @SuppressWarnings("deprecation")
    private ListWithFilter(
            @NotNull JList<T> list,
            @NotNull JScrollPane scrollPane,
            @Nullable Function<? super T, String> namer,
            boolean highlightAllOccurrences
    ) {
        super(new BorderLayout());

        if (list instanceof ComponentWithEmptyText) {
            ((ComponentWithEmptyText) list).getEmptyText().setText(UIBundle.message("message.noMatchesFound"));
        }

        myList = list;
        myScrollPane = scrollPane;

        mySearchField.getTextEditor().setFocusable(false);
        mySearchField.setVisible(false);

        add(mySearchField, BorderLayout.NORTH);
        add(myScrollPane, BorderLayout.CENTER);

        mySpeedSearch = new MySpeedSearch(highlightAllOccurrences);
        mySpeedSearch.setEnabled(namer != null);

        myList.addKeyListener(mySpeedSearch);
        int selectedIndex = myList.getSelectedIndex();
        int modelSize = myList.getModel().getSize();
        // DEPRECATED: replacement appeared in 2019-03-04
        myModel = new NameFilteringListModel<T>(myList, namer, mySpeedSearch::shouldBeShowing, mySpeedSearch);
        if (myModel.getSize() == modelSize) {
            myList.setSelectedIndex(selectedIndex);
        }

        setBackground(list.getBackground());
        //setFocusable(true);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        super.processFocusEvent(e);
        if (e.getID() == FocusEvent.FOCUS_GAINED) {
            IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> {
                IdeFocusManager.getGlobalInstance().requestFocus(myList, true);
            });
        }
    }

    public boolean resetFilter() {
        boolean hadPattern = mySpeedSearch.isHoldingFilter();
        if (mySearchField.isVisible()) {
            mySpeedSearch.reset();
        }
        return hadPattern;
    }

    public SpeedSearch getSpeedSearch() {
        return mySpeedSearch;
    }

    private class MySpeedSearch extends SpeedSearch {
        boolean searchFieldShown;
        boolean myInUpdate;

        MySpeedSearch(boolean highlightAllOccurrences) {
            super(highlightAllOccurrences);
            // native mac "clear button" is not captured by SearchTextField.onFieldCleared
            mySearchField.addDocumentListener(new DocumentAdapter() {
                @Override
                protected void textChanged(@NotNull DocumentEvent e) {
                    if (myInUpdate) return;
                    if (mySearchField.getText().isEmpty()) {
                        mySpeedSearch.reset();
                    }
                }
            });
            installSupplyTo(myList);
        }

        @Override
        public void update() {
            myInUpdate = true;
            mySearchField.getTextEditor().setBackground(UIUtil.getTextFieldBackground());
            onSpeedSearchPatternChanged();
            mySearchField.setText(getFilter());
            if (isHoldingFilter() && !searchFieldShown) {
                mySearchField.setVisible(true);
                searchFieldShown = true;
            } else if (!isHoldingFilter() && searchFieldShown) {
                mySearchField.setVisible(false);
                searchFieldShown = false;
            }

            myInUpdate = false;
            revalidate();
        }

        @Override
        public void noHits() {
            mySearchField.getTextEditor().setBackground(LightColors.RED);
        }

        private void revalidate() {
            JBPopup popup = PopupUtil.getPopupContainerFor(mySearchField);
            if (popup != null) {
                popup.pack(false, myAutoPackHeight);
            }
            ListWithFilter.this.revalidate();
        }
    }

    protected void onSpeedSearchPatternChanged() {
        T prevSelection = myList.getSelectedValue(); // save to restore the selection on filter drop
        myModel.refilter();
        if (myModel.getSize() > 0) {
            int fullMatchIndex = mySpeedSearch.isHoldingFilter() ? myModel.getClosestMatchIndex() : myModel.getElementIndex(prevSelection);
            if (fullMatchIndex != -1) {
                myList.setSelectedIndex(fullMatchIndex);
            }

            if (myModel.getSize() <= myList.getSelectedIndex() || !myModel.contains(myList.getSelectedValue())) {
                myList.setSelectedIndex(0);
            }
        } else {
            mySpeedSearch.noHits();
            revalidate();
        }
    }

    @NotNull
    public JList<? extends T> getList() {
        return myList;
    }

    public JScrollPane getScrollPane() {
        return myScrollPane;
    }

    public void setAutoPackHeight(boolean autoPackHeight) {
        myAutoPackHeight = autoPackHeight;
    }

    @Override
    public void requestFocus() {
        IdeFocusManager.getGlobalInstance().doWhenFocusSettlesDown(() -> {
            IdeFocusManager.getGlobalInstance().requestFocus(myList, true);
        });
    }
}
