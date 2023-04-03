package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public interface DrawableShape extends TransformableShape {
    @NotNull
    @Override
    DrawableShape transformedBy(@NotNull Transform transform);

    @NotNull
    @Override
    DrawableShape transformedBoundsBy(@NotNull Transform transform);

    @NotNull
    BufferedImage drawShape(@NotNull BufferedImage surface, final boolean isSelected, float dashPhase);

    @NotNull
    BufferedImage punchOutShape(@NotNull BufferedImage surface, @Nullable BufferedImage outerFill, @NotNull DrawingShape outerShape, boolean applyOuterFillToSurface);


    // test if inside shape
    boolean isInside(@Nullable Point point);
}
