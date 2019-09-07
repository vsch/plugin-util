package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public interface SelectableDrawableShape extends DrawableShape {
    @NotNull
    default BufferedImage drawShape(@NotNull BufferedImage surface) {
        return drawShape(surface, false, 0f);
    }

    @NotNull BufferedImage drawShape(@NotNull BufferedImage surface, final boolean isSelected, float dashPhase);

    @NotNull
    @Override
    SelectableDrawableShape transformedBy(@NotNull Transform transform);
}
