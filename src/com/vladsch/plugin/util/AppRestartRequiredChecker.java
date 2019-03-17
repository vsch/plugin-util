/*
 * Copyright (c) 2015-2019 Vladimir Schneider <vladimir.schneider@gmail.com>, all rights reserved.
 *
 * This code is private property of the copyright holder and cannot be used without
 * having obtained a license or prior written permission of the of the copyright holder.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package com.vladsch.plugin.util;

import com.intellij.ide.IdeBundle;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.diagnostic.Logger.getInstance;

@SuppressWarnings("WeakerAccess")
public abstract class AppRestartRequiredChecker<T> {
    static final Logger LOG = getInstance("com.vladsch.plugin.util.restart");
    
    boolean restartCheckPending = false;
    long restartNeededShownFlags = 0;
    final String restartNeededTitle;

    public AppRestartRequiredChecker() {
        this(UtilBundle.message("ide.restart.required.title"));
    }

    public AppRestartRequiredChecker(final String restartNeededTitle) {
        this.restartNeededTitle = restartNeededTitle;
    }

    public long getRestartNeededShownFlags() {
        return restartNeededShownFlags;
    }

    public boolean haveRestartNeededShownFlags(final long restartNeededShownFlags) {
        return (this.restartNeededShownFlags & restartNeededShownFlags) != 0;
    }

    public void setRestartNeededShownFlags(final long restartNeededShownFlags) {
        this.restartNeededShownFlags = restartNeededShownFlags;
    }

    public void setRestartNeededShownFlags(final long mask, final long restartNeededShownFlags) {
        this.restartNeededShownFlags &= ~mask;
        this.restartNeededShownFlags |= (mask & restartNeededShownFlags);
    }

    public void setRestartNeededShownFlags(final long mask, final boolean value) {
        if (value) this.restartNeededShownFlags |= mask;
        else this.restartNeededShownFlags &= ~mask;
    }

    public void addRestartNeededShownFlags(final long restartNeededShownFlags) {
        this.restartNeededShownFlags |= restartNeededShownFlags;
    }

    public void removeRestartNeededShownFlags(final long restartNeededShownFlags) {
        this.restartNeededShownFlags &= ~restartNeededShownFlags;
    }

    protected abstract long getRestartNeededReasons(T settings);

    @NotNull
    protected String getRestartMessage(@NotNull String action) {
        return UtilBundle.message("ide.restart.required.message", action, ApplicationNamesInfo.getInstance().getFullProductName());
    }

    public void informRestartIfNeeded(T settings) {
        if (!restartCheckPending) {
            restartCheckPending = true;
            ApplicationManagerEx.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    restartCheckPending = false;
                    long restartNeededReasons = getRestartNeededReasons(settings);

                    if ((restartNeededReasons & ~restartNeededShownFlags) != 0) {
                        if (showRestartDialog() == Messages.YES) {
                            ApplicationManagerEx.getApplicationEx().restart(true);
                        } else {
                            restartNeededShownFlags = restartNeededReasons;
                        }
                    } else {
                        restartNeededShownFlags = restartNeededReasons;
                    }
                }
            }, ModalityState.NON_MODAL);
        }
    }

    @Messages.YesNoResult
    int showRestartDialog() {
        String action = IdeBundle.message(ApplicationManagerEx.getApplicationEx().isRestartCapable() ? "ide.restart.action" : "ide.shutdown.action");
        String message = getRestartMessage(action);
        return Messages.showYesNoDialog(message, restartNeededTitle, action, IdeBundle.message("ide.postpone.action"), Messages.getQuestionIcon());
    }
}
