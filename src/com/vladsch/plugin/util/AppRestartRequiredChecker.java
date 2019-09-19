package com.vladsch.plugin.util;

import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Predicate;

import static com.intellij.openapi.diagnostic.Logger.getInstance;

@SuppressWarnings("WeakerAccess")
public class AppRestartRequiredChecker<T> {
    static final Logger LOG = getInstance("com.vladsch.plugin.util.restart");

    private final AppRestartRequiredCheckerBase<T> myRestartRequiredChecker;
    private final ArrayList<Predicate<T>> myRestartPredicates = new ArrayList<>();

    public AppRestartRequiredChecker() {
        this(UtilBundle.message("ide.restart.required.title"));
    }

    public AppRestartRequiredChecker(final String restartNeededTitle) {
        myRestartRequiredChecker = new AppRestartRequiredCheckerBase<T>(restartNeededTitle) {
            @Override
            protected long getRestartNeededReasons(final T settings) {
                return AppRestartRequiredChecker.this.getRestartNeededReasons(settings);
            }

            @NotNull
            @Override
            protected String getRestartMessage(@NotNull final String action) {
                return AppRestartRequiredChecker.this.getRestartMessage(action);
            }

            @Override
            int showRestartDialog() {
                return AppRestartRequiredChecker.this.showRestartDialog();
            }
        };
    }

    public void clear() {
       myRestartPredicates.clear();
    }

    long getRestartNeededReasons(T setting) {
        int iMax = myRestartPredicates.size();
        long mask = 1;
        long flags = 0;
        for (Predicate<T> predicate : myRestartPredicates) {
            if (predicate.test(setting)) {
                flags |= mask;
            }
            mask <<= 1;
        }
        return flags;
    }

    private long getPredicateMask(final @Nullable Predicate<T> predicate) {
        int i = predicate == null ? -1 : myRestartPredicates.indexOf(predicate);
        return i >= 0 ? 1 << i : 0;
    }

    public boolean haveRestartNeededShownFlags(final @Nullable Predicate<T> predicate) {
        return myRestartRequiredChecker.haveRestartNeededShownFlags(getPredicateMask(predicate));
    }

    public void setRestartNeededShownFlags(final @Nullable Predicate<T> predicate) {
        myRestartRequiredChecker.setRestartNeededShownFlags(getPredicateMask(predicate));
    }

    public void setRestartNeededShownFlags(final @Nullable Predicate<T> predicate, boolean value) {
        myRestartRequiredChecker.setRestartNeededShownFlags(getPredicateMask(predicate), value);
    }

    public void addRestartNeededPredicate(final Predicate<T> predicate) {
        assert myRestartPredicates.size() < 63 : "Maximum number of restart checker predicates is 63";
        myRestartPredicates.add(predicate);
    }

    @NotNull
    protected String getRestartMessage(@NotNull String action) {
        return UtilBundle.message("ide.restart.required.message", action, ApplicationNamesInfo.getInstance().getFullProductName());
    }

    public void informRestartIfNeeded(T settings) {
        myRestartRequiredChecker.informRestartIfNeeded(settings);
    }

    @Messages.YesNoResult
    int showRestartDialog() {
        String action = IdeBundle.message(ApplicationManagerEx.getApplicationEx().isRestartCapable() ? "ide.restart.action" : "ide.shutdown.action");
        String message = getRestartMessage(action);
        return Messages.showYesNoDialog(message, myRestartRequiredChecker.restartNeededTitle, action, IdeBundle.message("ide.postpone.action"), Messages.getQuestionIcon());
    }
}
