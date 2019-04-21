package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public interface DrawingTransform extends ImageTransform {
    @NotNull BufferedImage transform(@NotNull BufferedImage image);

    final DrawingTransform NULL = new DrawingTransform() {
        @NotNull
        @Override
        public BufferedImage transform(@NotNull final BufferedImage image) {
            return ImageUtils.toBufferedImage(image);
        }

        @NotNull
        @Override
        public Rectangle transformImage(@NotNull final Rectangle rectangle) {
            return rectangle;
        }

        @NotNull
        @Override
        public Rectangle reverseImage(@NotNull final Rectangle rectangle) {
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
        public DrawingTransform reversed() {
            return this;
        }
    };
}
