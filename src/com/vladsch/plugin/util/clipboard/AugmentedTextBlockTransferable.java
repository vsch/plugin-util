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

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.codeInsight.editorActions.TextBlockTransferableData;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.RawText;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.intellij.openapi.diagnostic.Logger.getInstance;
import static java.awt.datatransfer.DataFlavor.stringFlavor;

@SuppressWarnings("WeakerAccess")
public class AugmentedTextBlockTransferable implements Transferable {
    private static final Logger LOG = getInstance("com.vladsch.plugin.util.clipboard");
    final static public Object DATA = new Object();

    final static public DataFlavor[] EMPTY_DATA_FLAVORS = new DataFlavor[0];
    final static public Object[] EMPTY_OBJECTS = new Object[0];

    final private Object[] myNonTextBlockData;
    final private DataFlavor[] myNonTextBlockFlavours;
    final private TextBlockTransferable myTextBlockTransferable;

    AugmentedTextBlockTransferable(@NotNull Transferable transferable, TextBlockTransferableData... extraData) {
        ArrayList<DataFlavor> nonTextBlockFlavours = new ArrayList<>();
        ArrayList<Object> nonTextBlockData = new ArrayList<>();
        ArrayList<TextBlockTransferableData> textBlockData = new ArrayList<>();
        String text = null;
        RawText rawText = null;
        TextBlockDataFlavorRegistrar flavorRegistrar = TextBlockDataFlavorRegistrar.getInstance();
        DataFlavor augmentedDataFlavor = flavorRegistrar.getAugmentedDataFlavor();
        String augmentedDataFlavorMimeType = augmentedDataFlavor.getMimeType();

        for (DataFlavor flavor : transferable.getTransferDataFlavors()) {
            if (flavor.getMimeType().equals(augmentedDataFlavorMimeType)) continue;

            try {
                Object data = flavorRegistrar.getTransferData(transferable, flavor);

                if (data instanceof TextBlockTransferableData) {
                    LOG.info("adding text-block: " + flavor + " data: " + data);
                    textBlockData.add((TextBlockTransferableData) data);
                } else {
                    if (flavor.equals(stringFlavor)) {
                        // this one will be the text
                        text = (String) data;
                    } else if (data instanceof RawText) {
                        if (rawText == null) {
                            rawText = (RawText) data;
                        }
                    } else {
                        LOG.info("adding non-text-block: " + flavor + " data: " + data);
                        nonTextBlockFlavours.add(flavor);
                        nonTextBlockData.add(data);
                    }
                }
            } catch (Exception e) {
                LOG.info("Exception getting data for flavor " + flavor, e);
            }
        }

        // add dummies to allow using isDataFlavorAvailable to test for augmented content
        nonTextBlockFlavours.add(augmentedDataFlavor);
        nonTextBlockData.add(DATA);

        if (text == null) text = "";
        textBlockData.addAll(Arrays.asList(extraData));
        myTextBlockTransferable = new TextBlockTransferable(text, textBlockData, rawText);
        myNonTextBlockFlavours = nonTextBlockFlavours.toArray(EMPTY_DATA_FLAVORS);
        myNonTextBlockData = nonTextBlockData.toArray(EMPTY_OBJECTS);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = myTextBlockTransferable.getTransferDataFlavors();
        DataFlavor[] combinedFlavours = new DataFlavor[flavors.length + myNonTextBlockFlavours.length];
        if (flavors.length > 0) System.arraycopy(flavors, 0, combinedFlavours, 0, flavors.length);
        if (myNonTextBlockFlavours.length > 0) System.arraycopy(myNonTextBlockFlavours, 0, combinedFlavours, flavors.length, myNonTextBlockFlavours.length);
        return combinedFlavours;
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        if (myTextBlockTransferable.isDataFlavorSupported(flavor)) return true;
        for (DataFlavor f : myNonTextBlockFlavours) {
            if (f.equals(flavor)) return true;
        }
        return false;
    }

    @NotNull
    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (myTextBlockTransferable.isDataFlavorSupported(flavor)) return myTextBlockTransferable.getTransferData(flavor);

        int iMax = myNonTextBlockFlavours.length;
        for (int i = 0; i < iMax; i++) {
            if (flavor.equals(myNonTextBlockFlavours[i])) return myNonTextBlockData[i];
        }

        throw new UnsupportedFlavorException(flavor);
    }
}
