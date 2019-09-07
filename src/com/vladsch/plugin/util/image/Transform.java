package com.vladsch.plugin.util.image;

import org.jetbrains.annotations.NotNull;

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

    Transform NULL = new Transform() {
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
    };
}
