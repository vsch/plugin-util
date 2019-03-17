/*
 * Copyright (c) 2016-2019 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.vladsch.plugin.util.clipboard;

import com.intellij.codeInsight.editorActions.TextBlockTransferableData;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.SystemFlavorMap;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.BiFunction;

public class TextBlockDataFlavorRegistrar {
    private static final Logger LOG = Logger.getInstance("com.vladsch.plugin.util.clipboard");

    public static TextBlockDataFlavorRegistrar getInstance() {
        return ApplicationManager.getApplication().getComponent(TextBlockDataFlavorRegistrar.class);
    }

    private HashMap<String, DataFlavor> myCreatedFlavours;
    private HashMap<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> myTransferDataLoaders;

    public TextBlockDataFlavorRegistrar() {
        myCreatedFlavours = PluginUtilSharedResources.getCreatedFlavours();
        myTransferDataLoaders = PluginUtilSharedResources.getTransferDataLoaders();
    }

    public DataFlavor getAugmentedDataFlavor() {
        DataFlavor dataFlavor = PluginUtilSharedResources.getAugmentedDataFlavor();
        return dataFlavor;
    }

    @NotNull
    public Transferable augmentTransferable(@NotNull Transferable transferable, TextBlockTransferableData... extraData) {
        return new AugmentedTextBlockTransferable(transferable, extraData);
    }

    @NotNull
    public Object getTransferData(@NotNull final Transferable transferable, @NotNull final DataFlavor flavor) throws IOException, UnsupportedFlavorException {
        BiFunction<Transferable, DataFlavor, Object> dataLoader = myTransferDataLoaders.get(flavor);
        if (dataLoader != null) {
            return dataLoader.apply(transferable, flavor);
        }

        return transferable.getTransferData(flavor);
    }

    @NotNull
    public DataFlavor getOrCreateDataFlavor(@NotNull final String mimeType, @NotNull final String humanPresentableName, @Nullable final Class<?> klass, final ClassLoader classLoader, final boolean registerNative, @Nullable BiFunction<Transferable, DataFlavor, Object> dataLoader) {
        try {
            final DataFlavor flavor =
                    klass != null ? new DataFlavor(mimeType + ";class=" + klass.getName(), humanPresentableName, classLoader == null ? klass.getClassLoader() : classLoader) : new DataFlavor(mimeType);

            if (registerNative) {
                final FlavorMap map = SystemFlavorMap.getDefaultFlavorMap();
                if (map instanceof SystemFlavorMap) {
                    String nat = SystemFlavorMap.encodeDataFlavor(flavor);

                    DataFlavor dataFlavor = myCreatedFlavours.get(nat);
                    if (dataFlavor != null) return dataFlavor;

                    ((SystemFlavorMap) map).addUnencodedNativeForFlavor(flavor, nat);
                    ((SystemFlavorMap) map).addFlavorForUnencodedNative(nat, flavor);
                    myCreatedFlavours.put(nat, flavor);
                }
            }

            if (dataLoader != null) myTransferDataLoaders.put(flavor, dataLoader);

            return flavor;
        } catch (ClassNotFoundException e) {
            LOG.error(e);
            throw new IllegalStateException("Invalid Class/ClassLoader passed to createDataFlavour " + klass + " " + classLoader);
        }
    }
}
