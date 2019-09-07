package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

public interface Transform {
    @NotNull Rectangle transformImage(@NotNull Rectangle rectangle);
    @NotNull Rectangle reverseImage(@NotNull Rectangle rectangle);

    @NotNull Rectangle transform(@NotNull Rectangle rectangle);
    @NotNull Rectangle reverse(@NotNull Rectangle rectangle);
    @NotNull Point transform(@NotNull Point point);
    @NotNull Point reverse(@NotNull Point point);

    @NotNull
    Transform reversed();

    Transform NULL = new Transform() {
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
        public Transform reversed() {
            return this;
        }
    };
}
