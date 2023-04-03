package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

public interface TransformableShape {
    @NotNull
    TransformableShape transformedBy(@NotNull Transform transform, final Rectangle bounds);

    @NotNull
    TransformableShape transformedBoundsBy(@NotNull Transform transform);

    boolean isEmpty();
}
