package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public interface Transform {
    @NotNull
    default Transform andThen(@NotNull Transform transform) {
        return new TransformList(Arrays.asList(transform, this), false);
    }

    // imaging surface bounds transformations
    @NotNull
    Rectangle transformBounds(@NotNull Rectangle rectangle);

    default @NotNull
    Rectangle reverseBounds(@NotNull Rectangle rectangle) {
        return reversed().transformBounds(rectangle);
    }

    // shapes on imaging surface transformations
    @NotNull
    Rectangle transform(@NotNull Rectangle rectangle);

    default @NotNull
    Rectangle reverse(@NotNull Rectangle rectangle) {
        return reversed().transform(rectangle);
    }

    @NotNull
    Point transform(@NotNull Point point);

    default @NotNull
    Point reverse(@NotNull Point point) {
        return reversed().transform(point);
    }

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
