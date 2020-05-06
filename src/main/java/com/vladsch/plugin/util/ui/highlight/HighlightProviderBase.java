package com.vladsch.plugin.util.ui.highlight;

import com.intellij.ide.ui.LafManager;
import com.intellij.ide.ui.LafManagerListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.messages.MessageBusConnection;
import com.vladsch.plugin.util.AwtRunnable;
import com.vladsch.plugin.util.CancelableJobScheduler;
import com.vladsch.plugin.util.DelayedRunner;
import com.vladsch.plugin.util.OneTimeRunnable;
import com.vladsch.plugin.util.ui.ColorIterable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.UIManager;
import java.awt.Color;
import java.util.HashSet;

public abstract class HighlightProviderBase<T> implements HighlightProvider<T>, Disposable {
    protected final LafManagerListener myLafManagerListener;
    protected Color[] myHighlightColors;
    protected int myHighlightColorRepeatIndex;
    final protected @NotNull DelayedRunner myDelayedRunner;
    protected boolean myHighlightsMode = true;
    protected int myInUpdateRegion = 0;
    protected boolean myPendingChanged = false;
    protected @NotNull T mySettings;
    private int myInSet = 0;
    private int mySavedHighlightIndex = -1;

    private OneTimeRunnable myHighlightRunner = OneTimeRunnable.NULL;
    private final HashSet<HighlightListener> myHighlightListeners;

    public HighlightProviderBase(@NotNull T settings) {
        mySettings = settings;

        myDelayedRunner = new DelayedRunner();
        myHighlightListeners = new HashSet<>();
        myLafManagerListener = new LafManagerListener() {
            UIManager.LookAndFeelInfo lookAndFeel = LafManager.getInstance().getCurrentLookAndFeel();

            @Override
            public void lookAndFeelChanged(@NotNull final LafManager source) {
                UIManager.LookAndFeelInfo newLookAndFeel = source.getCurrentLookAndFeel();
                if (lookAndFeel != newLookAndFeel) {
                    lookAndFeel = newLookAndFeel;
                    settingsChanged(getColors(mySettings), mySettings);
                }
            }
        };
    }

    @NotNull
    public DelayedRunner getDelayedRunner() {
        return myDelayedRunner;
    }

    @Nullable
    protected abstract CancelableJobScheduler getCancellableJobScheduler();

    protected abstract void subscribeSettingsChanged();

    @NotNull
    protected abstract ColorIterable getColors(@NotNull T settings);

    @SuppressWarnings("deprecation")
    public void initComponent() {
        MessageBusConnection settingsConnection = ApplicationManager.getApplication().getMessageBus().connect(this);

        try {
            settingsConnection.subscribe(LafManagerListener.TOPIC, myLafManagerListener);
        } catch (NoSuchFieldError ignored) {
            // DEPRECATED: replacement appeared in 2019-07-20
            LafManager.getInstance().addLafManagerListener(myLafManagerListener);
            myDelayedRunner.addRunnable(() -> {
                // DEPRECATED: replacement appeared in 2019-07-20
                LafManager.getInstance().removeLafManagerListener(myLafManagerListener);
            });
        }

        settingsChanged(getColors(mySettings), mySettings);
    }

    public void disposeComponent() {
        myDelayedRunner.runAll();
    }

    @Override
    public void dispose() {
        disposeComponent();
    }

    @Override
    public void settingsChanged(final ColorIterable colors, final T settings) {
        ColorIterable.ColorIterator iterator = colors.iterator();

        myHighlightColors = new Color[iterator.getMaxIndex()];
        while (iterator.hasNext()) {
            Color hsbColor = iterator.next();
            myHighlightColors[iterator.getIndex()] = hsbColor;
        }

        myHighlightColorRepeatIndex = myHighlightColors.length - iterator.getHueSteps();

        fireHighlightsChanged();
    }

    @Override
    public void addHighlightListener(@NotNull HighlightListener highlightListener, @NotNull Disposable parent) {
        if (!myHighlightListeners.contains(highlightListener)) {
            myHighlightListeners.add(highlightListener);
            Disposer.register(parent, () -> myHighlightListeners.remove(highlightListener));
        }
    }

    @Override
    public void removeHighlightListener(@NotNull HighlightListener highlightListener) {
        myHighlightListeners.remove(highlightListener);
    }

    public void startHighlightSet() {
        startHighlightSet(0);
    }

    abstract protected void skipHighlightSets(int skipSets);
    abstract protected void setHighlightIndex(int index);
    abstract protected int getHighlightIndex();

    public boolean isInHighlightSet() {
        return myInSet > 0;
    }

    public void startHighlightSet(int skipSets) {
        if (skipSets > 0) skipHighlightSets(skipSets);
        if (!isInHighlightSet()) {
            mySavedHighlightIndex = -1;
        }
        myInSet++;
    }

    public void restartHighlightSet(int index) {
        if (index >= 0) {
            if (!isInHighlightSet()) {
                mySavedHighlightIndex = getHighlightIndex();
            }
            setHighlightIndex(index);
        }
        myInSet++;
    }

    public void endHighlightSet() {
        if (myInSet > 0) {
            myInSet--;
            if (!isInHighlightSet()) {
                if (mySavedHighlightIndex != -1) {
                    setHighlightIndex(mySavedHighlightIndex);
                    mySavedHighlightIndex = -1;
                } else {
                    skipHighlightSets(1);
                }
            }
        }
    }

    @Override
    public void enterUpdateRegion() {
        if (myInUpdateRegion++ == 0) {
            myPendingChanged = false;
        }
    }

    @Override
    public void leaveUpdateRegion() {
        if (--myInUpdateRegion <= 0) {
            if (myInUpdateRegion < 0) {
                throw new IllegalStateException("InUpdateRegion < 0, " + myInUpdateRegion);
            }
            if (myPendingChanged) {
                myPendingChanged = false;
                fireHighlightsChanged();
            }
        }
    }

    @Override
    public void fireHighlightsChanged() {
        myHighlightRunner.cancel();
        if (myInUpdateRegion <= 0) {
            if (!myHighlightListeners.isEmpty()) {
                CancelableJobScheduler scheduler = getCancellableJobScheduler();
                if (scheduler != null) {
                    myHighlightRunner = OneTimeRunnable.schedule(scheduler, 250, new AwtRunnable(true, () -> {
                        for (HighlightListener listener : myHighlightListeners) {
                            if (listener == null) continue;
                            listener.highlightsChanged();
                        }
                    }));
                }
            }
        } else {
            myPendingChanged = true;
        }
    }

    @Override
    public void fireHighlightsUpdated() {
        myHighlightRunner.cancel();
        if (myInUpdateRegion <= 0) {
            if (!myHighlightListeners.isEmpty()) {
                for (HighlightListener listener : myHighlightListeners) {
                    if (listener == null) continue;
                    listener.highlightsUpdated();
                }
            }
        } else {
            myPendingChanged = true;
        }
    }

    public int getHighlightColorRepeatIndex() {
        return myHighlightColorRepeatIndex;
    }

    public Color[] getHighlightColors() {
        return myHighlightColors;
    }

    public boolean isHighlightsMode() {
        return myHighlightsMode;
    }

    public void setHighlightsMode(final boolean highlightsMode) {
        myHighlightsMode = highlightsMode;
        if (haveHighlights()) {
            fireHighlightsChanged();
        }
    }
}
