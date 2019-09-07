package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

public interface TransformableShape {
    @NotNull
    TransformableShape transformedBy(@NotNull Transform transform);

    boolean isEmpty();
}
