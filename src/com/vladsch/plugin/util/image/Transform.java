package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public interface Transform {
    // imaging surface bounds transformations
    // @formatter:off
    @NotNull Rectangle transformBounds(@NotNull Rectangle rectangle);
    @NotNull Rectangle reverseBounds(@NotNull Rectangle rectangle);

    // shapes on imaging surface transformations
    @NotNull Rectangle transform(@NotNull Rectangle rectangle);
    @NotNull Rectangle reverse(@NotNull Rectangle rectangle);
    @NotNull Point transform(@NotNull Point point);
    @NotNull Point reverse(@NotNull Point point);
    // @formatter:on

    @NotNull
    Transform reversed();

    @NotNull
    BufferedImage transform(@NotNull BufferedImage image);

    @NotNull
    default DrawingShape imageBorders(@NotNull DrawingShape shape) {
        return shape.transformedBoundsBy(this);
    }

    boolean isEmpty();

    Transform NULL = new Transform() {
        @NotNull
        @Override
        public BufferedImage transform(@NotNull final BufferedImage image) {
            return ImageUtils.toBufferedImage(image);
        }

        @NotNull
        @Override
        public Rectangle transformBounds(@NotNull final Rectangle rectangle) {
            return rectangle;
        }

        @NotNull
        @Override
        public Rectangle reverseBounds(@NotNull final Rectangle rectangle) {
            return rectangle;
        }

        @NotNull
        @Override
        public Rectangle transform(@NotNull final Rectangle rectangle) {
            return rectangle;
        }

        @NotNull
        @Override
        public Rectangle reverse(@NotNull final Rectangle rectangle) {
            return rectangle;
        }

        @NotNull
        @Override
        public Point transform(@NotNull final Point point) {
            return point;
        }

        @NotNull
        @Override
        public Point reverse(@NotNull final Point point) {
            return point;
        }

        @NotNull
        @Override
        public Transform reversed() {
            return this;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    };
}
