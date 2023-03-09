package com.vladsch.plugin.util.clipboard;

import com.intellij.codeInsight.editorActions.TextBlockTransferable;
import com.intellij.codeInsight.editorActions.TextBlockTransferableData;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.RawText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

@SuppressWarnings("WeakerAccess")
public class AugmentedTextBlockTransferable implements Transferable {
    private static final Logger LOG = ClipboardUtils.LOG;

    public static final String AUGMENTED_MIME_TYPE = "application/augmented-transferable";
    private static final DataFlavor DATA_FLAVOR = ClipboardUtils.createDataFlavor(AUGMENTED_MIME_TYPE, "Augmented Transferable for Sharing", null, null, false);
    private static final Object DATA = new Object();

    private static final BiFunction<Transferable, DataFlavor, Object> NULL_LOADER = (transferable, flavor) -> {
        try {
            return transferable.getTransferData(flavor);
        } catch (UnsupportedFlavorException e) {
            LOG.error(e);
        } catch (IOException e) {
            LOG.error(e);
        }
        return null;
    };

    final static public DataFlavor[] EMPTY_DATA_FLAVORS = new DataFlavor[0];
    final static public Object[] EMPTY_OBJECTS = new Object[0];
    final static public TextBlockTransferableData[] EMPTY_TEXT_BLOCK_TRANSFERABLE_DATA = new TextBlockTransferableData[0];

    public static boolean isAugmentedDataFlavor(@NotNull DataFlavor flavor) {
        return AUGMENTED_MIME_TYPE.equals(flavor.getMimeType());
    }

    @Nullable
    public static Map<String, BiFunction<Transferable, DataFlavor, Object>> getDataLoaders(@NotNull Transferable transferable) {
        if (transferable.isDataFlavorSupported(DATA_FLAVOR)) {
            try {
                //noinspection unchecked
                return (Map<String, BiFunction<Transferable, DataFlavor, Object>>) transferable.getTransferData(DATA_FLAVOR);
            } catch (UnsupportedFlavorException e) {
                LOG.error(e);
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        return null;
    }

    public static boolean isAugmented(@NotNull Transferable transferable) {
        return transferable.isDataFlavorSupported(DATA_FLAVOR);
    }

    final private @NotNull Object[] myNonTextBlockData;
    final private @NotNull DataFlavor[] myNonTextBlockFlavours;
    final private @NotNull TextBlockTransferable myTextBlockTransferable;
    final private @Nullable Map<String, BiFunction<Transferable, DataFlavor, Object>> myDataLoaders;

    private AugmentedTextBlockTransferable(
            @Nullable Map<String, BiFunction<Transferable, DataFlavor, Object>> dataLoaders,
            @NotNull String text,
            @NotNull Collection<TextBlockTransferableData> textBlockData,
            @NotNull Map<DataFlavor, Object> nonTextBlocks,
            @Nullable RawText rawText
    ) {
        myDataLoaders = dataLoaders != null && !dataLoaders.isEmpty() ? dataLoaders : null;
        myTextBlockTransferable = new TextBlockTransferable(text, textBlockData, rawText);
        myNonTextBlockFlavours = nonTextBlocks.keySet().toArray(EMPTY_DATA_FLAVORS);
        myNonTextBlockData = nonTextBlocks.values().toArray(EMPTY_OBJECTS);
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
        if (isAugmentedDataFlavor(flavor)) return true;

        if (myTextBlockTransferable.isDataFlavorSupported(flavor)) return true;
        for (DataFlavor f : myNonTextBlockFlavours) {
            if (f.equals(flavor)) return true;
        }
        return false;
    }

    @NotNull
    private static Object getTransferData(@Nullable BiFunction<Transferable, DataFlavor, Object> dataLoader, @NotNull Transferable transferable, final @NotNull DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        Object data = dataLoader == null ? transferable.getTransferData(flavor) : dataLoader.apply(transferable, flavor);
        if (data != null) return data;

        throw new UnsupportedFlavorException(flavor);
    }

    @NotNull
    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isAugmentedDataFlavor(flavor)) return myDataLoaders == null ? Collections.EMPTY_MAP : myDataLoaders;

        BiFunction<Transferable, DataFlavor, Object> dataLoader = myDataLoaders == null ? NULL_LOADER : myDataLoaders.get(flavor.getMimeType());

        if (myTextBlockTransferable.isDataFlavorSupported(flavor)) return getTransferData(dataLoader, myTextBlockTransferable, flavor);

        int iMax = myNonTextBlockFlavours.length;
        for (int i = 0; i < iMax; i++) {
            if (flavor.equals(myNonTextBlockFlavours[i])) return myNonTextBlockData[i];
        }

        throw new UnsupportedFlavorException(flavor);
    }

    public static AugmentedTextBlockTransferable create(
            @NotNull Transferable transferable,
            @NotNull Map<TextBlockTransferableData, BiFunction<Transferable, DataFlavor, Object>> extraBlockData,
            @Nullable Map<String, BiFunction<Transferable, DataFlavor, Object>> extraDataLoaders
    ) {
        if (extraDataLoaders != null) extraDataLoaders = new HashMap<>(extraDataLoaders);

        HashMap<String, TextBlockTransferableData> extraData = new HashMap<>();

        for (TextBlockTransferableData data : extraBlockData.keySet()) {
            String mimeType = data.getFlavor().getMimeType();
            extraData.put(mimeType, data);

            BiFunction<Transferable, DataFlavor, Object> loader = extraBlockData.get(data);
            if (loader != null) {
                if (extraDataLoaders == null) extraDataLoaders = new HashMap<>();
                extraDataLoaders.put(mimeType, loader);
            }
        }

        return create(transferable, extraDataLoaders, extraData.values().toArray(EMPTY_TEXT_BLOCK_TRANSFERABLE_DATA));
    }

    private static AugmentedTextBlockTransferable create(@NotNull Transferable transferable, @Nullable Map<String, BiFunction<Transferable, DataFlavor, Object>> extraDataLoaders, TextBlockTransferableData... extraData) {
        HashMap<DataFlavor, Object> nonTextBlocks = new HashMap<>();
        HashMap<String, TextBlockTransferableData> textBlockData = new HashMap<>();

        String text = null;
        RawText rawText = null;
        Map<String, BiFunction<Transferable, DataFlavor, Object>> dataLoaders = getDataLoaders(transferable);
        HashMap<String, BiFunction<Transferable, DataFlavor, Object>> loaders = new HashMap<>();

        for (DataFlavor flavor : transferable.getTransferDataFlavors()) {
            if (isAugmentedDataFlavor(flavor)) continue;

            try {
                Object data;
                BiFunction<Transferable, DataFlavor, Object> dataLoader = null;

                if (dataLoaders != null) {
                    dataLoader = dataLoaders.get(flavor.getMimeType());
                    if (dataLoader != null) {
                        data = dataLoader.apply(transferable, flavor);
                    } else if (extraDataLoaders != null) {
                        dataLoader = extraDataLoaders.get(flavor.getMimeType());
                        if (dataLoader != null) data = dataLoader.apply(transferable, flavor);
                        else {
                            data = transferable.getTransferData(flavor);
                        }
                    } else {
                        data = transferable.getTransferData(flavor);
                    }
                } else if (extraDataLoaders != null) {
                    dataLoader = extraDataLoaders.get(flavor.getMimeType());

                    if (dataLoader != null) data = dataLoader.apply(transferable, flavor);
                    else {
                        data = transferable.getTransferData(flavor);
                    }
                } else {
                    data = transferable.getTransferData(flavor);
                }

                if (data instanceof TextBlockTransferableData) {
                    if (LOG.isDebugEnabled()) LOG.debug("adding text-block: " + flavor + " data: " + data);
                    TextBlockTransferableData transferableData = (TextBlockTransferableData) data;
                    String mimeType = transferableData.getFlavor().getMimeType();
                    textBlockData.put(mimeType, transferableData);
                    if (dataLoader != null) loaders.put(mimeType, dataLoader);
                } else {
                    if (flavor.equals(stringFlavor)) {
                        // this one will be the text
                        text = (String) data;
                    } else if (data instanceof RawText) {
                        if (rawText == null) {
                            rawText = (RawText) data;
                        }
                    } else {
                        if (LOG.isDebugEnabled()) LOG.debug("adding non-text-block: " + flavor + " data: " + data);
                        nonTextBlocks.put(flavor, data);
                    }
                }
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) LOG.debug("Exception getting data for flavor " + flavor, e);
            }
        }

        if (text == null) text = "";

        if (extraData.length > 0) {
            for (TextBlockTransferableData transferableData : extraData) {
                textBlockData.put(transferableData.getFlavor().getMimeType(), transferableData);
            }
        }

        putAllIfMissing(loaders, dataLoaders);
        putAllIfMissing(loaders, extraDataLoaders);

        return new AugmentedTextBlockTransferable(loaders, text, textBlockData.values(), nonTextBlocks, rawText);
    }

    private static void putAllIfMissing(@NotNull HashMap<String, BiFunction<Transferable, DataFlavor, Object>> loaders, @Nullable Map<String, BiFunction<Transferable, DataFlavor, Object>> flavorLoaders) {
        if (flavorLoaders != null && !flavorLoaders.isEmpty()) {
            for (Map.Entry<String, BiFunction<Transferable, DataFlavor, Object>> entry : flavorLoaders.entrySet()) {
                if (!loaders.containsKey(entry.getKey())) loaders.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void putAllByMimeType(HashMap<String, BiFunction<Transferable, DataFlavor, Object>> loaders, Map<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> flavorLoaders) {
        for (Map.Entry<DataFlavor, BiFunction<Transferable, DataFlavor, Object>> entry : flavorLoaders.entrySet()) {
            loaders.put(entry.getKey().getMimeType(), entry.getValue());
        }
    }
}
