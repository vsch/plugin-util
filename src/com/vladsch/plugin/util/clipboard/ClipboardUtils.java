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

package com.vladsch.plugin.util.clipboard;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import static com.intellij.openapi.diagnostic.Logger.getInstance;

public class ClipboardUtils {
    public static final Logger LOG = getInstance("com.vladsch.plugin.util.clipboard");

    @NotNull
    public static DataFlavor createDataFlavor(@NotNull final String mimeType, @NotNull final String humanPresentableName, @Nullable final Class<?> klass, final ClassLoader classLoader, final boolean registerNative) {
        try {
            final DataFlavor flavor =
                    klass != null ? new DataFlavor(mimeType + ";class=" + klass.getName(), humanPresentableName, classLoader == null ? klass.getClassLoader() : classLoader) : new DataFlavor(mimeType);

            if (registerNative) {
                final FlavorMap map = SystemFlavorMap.getDefaultFlavorMap();
                if (map instanceof SystemFlavorMap) {
                    String nat = SystemFlavorMap.encodeDataFlavor(flavor);
                    ((SystemFlavorMap) map).addUnencodedNativeForFlavor(flavor, nat);
                    ((SystemFlavorMap) map).addFlavorForUnencodedNative(nat, flavor);
                }
            }

            return flavor;
        } catch (ClassNotFoundException e) {
            LOG.error(e);
            throw new IllegalStateException("Invalid Class/ClassLoader passed to createDataFlavour " + klass + " " + classLoader);
        }
    }
    
    @Nullable
    public static Clipboard getSystemClipboard() {
        try {
            return Toolkit.getDefaultToolkit().getSystemClipboard();
        } catch (IllegalStateException e) {
            if (SystemInfo.isWindows) {
                LOG.debug("Clipboard is busy");
            } else {
                LOG.warn(e);
            }
            return null;
        }
    }

    @Nullable
    public static Object getTransferDataOrNull(@NotNull Transferable transferable, @NotNull DataFlavor flavor) {
        try {
            return transferable.getTransferData(flavor);
        } catch (UnsupportedFlavorException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    }
}
