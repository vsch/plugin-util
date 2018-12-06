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

package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class CroppingTransform(margins: Rectangle) : CropTransform(margins), DrawingTransform {
    override fun transform(image: BufferedImage): BufferedImage {
        if (margins.isNull || transformImage(Rectangle.of(image)).isAnyNull) return image
        return ImageUtils.cropImage(image, margins.x0, margins.x1, margins.y0, margins.y1)
    }
    override fun reversed(): ImageTransform {
        return CroppingTransform(margins.scale(-1f));
    }
}
