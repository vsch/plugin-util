package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public interface ImageTransform extends Transform {
    @NotNull
    BufferedImage transform(@NotNull BufferedImage image);

    @Nullable
    default DrawingShape imageBorders(@NotNull BufferedImage image) {
        return null;
    }

    ImageTransform NULL = new ImageTransform() {
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
        public ImageTransform reversed() {
            return this;
        }
    };
}
