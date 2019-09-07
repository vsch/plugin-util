/*
 *
 */

package com.vladsch.plugin.util.image

import org.junit.Assert.assertArrayEquals
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

open class ImageTest {
    companion object {
        val rootDir = "/Users/vlad/src/projects/plugin-util/test-resources/"

        val TRANSPARENT = Color(0, 0, 0, 0)
        val TRANSLUCENT = Color(0, 0, 0, 64)
    }

    fun assertEquals(message: String, expected: BufferedImage?, actual: BufferedImage?) {
        val expectedBytes = ImageUtils.getImageBytes(expected)
        val actualBytes = ImageUtils.getImageBytes(actual)
        assertArrayEquals(message, expectedBytes, actualBytes)
    }

    fun assertEquals(expected: BufferedImage?, actual: BufferedImage?) {
        val expectedBytes = ImageUtils.getImageBytes(expected)
        val actualBytes = ImageUtils.getImageBytes(actual)
        assertArrayEquals(expectedBytes, actualBytes)
    }

    fun imageResourcePath(name: String): String {
        return rootDir + this.javaClass.canonicalName.replace('.', '/') + "/" + name + ".png"
    }

    fun imageFilePath(name: String): String {
        return rootDir + this.javaClass.canonicalName.replace('.', '/') + "/" + name + ".png"
    }

    fun getSourceImage(name: String): BufferedImage {
        val imagePath = imageResourcePath("original/$name")
        return ImageIO.read(File(imagePath))
    }

    fun getExpectedImage(name: String): BufferedImage {
        val imagePath = imageResourcePath("expected/$name")
        return ImageIO.read(File(imagePath))
    }

    fun saveImage(name: String, image: BufferedImage) {
        val imagePath = imageFilePath("actual/$name")
        ImageUtils.save(image, File(imagePath), "PNG")
    }

    fun assertImagesEqual(message: String, expectedName: String, actual: BufferedImage) {
        saveImage(expectedName, actual)
        val expected = getExpectedImage(expectedName)
        assertEquals(message, expected, actual)
    }

    fun assertImagesEqual(expectedName: String, actual: BufferedImage) {
        saveImage(expectedName, actual)
        val expected = getExpectedImage(expectedName)
        assertEquals(expected, actual)
    }
}
