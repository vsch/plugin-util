/*
 *
 */

package com.vladsch.plugin.util.image

import java.awt.image.BufferedImage

@Suppress("MemberVisibilityCanBePrivate")
class ImageTransformer constructor(transforms: List<Transform>, reversed: Boolean = false) : Transformer(transforms, reversed), ImageTransform {

    override fun transform(image: BufferedImage): BufferedImage {
        var result = image
        forEach { if (it is ImageTransform) result = it.transform(result) }
        return result
    }
}
