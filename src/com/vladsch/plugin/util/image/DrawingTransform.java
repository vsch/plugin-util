/*
 * Copyright (c) 2016-2018 Vladimir Schneider <vladimir.schneider@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
