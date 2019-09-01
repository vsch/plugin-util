/*
 *
 */

package com.vladsch.plugin.util.ui.highlight;

import java.util.EventListener;

public interface HighlightListener extends EventListener {
    default void highlightsChanged() { }

    default void highlightsUpdated() { }

}
