package com.vladsch.plugin.util

import com.intellij.openapi.vfs.VirtualFile
import java.security.DigestInputStream
import java.security.MessageDigest

class Md5Utils {
    companion object {
        @JvmStatic
        fun md5(virtualFile: VirtualFile): String {
            val md5 = MessageDigest.getInstance("MD5")

            val buffer = ByteArray(16384)
            val md5IS = DigestInputStream(virtualFile.inputStream, md5)
            while (md5IS.read(buffer) > 0);

            val sb = StringBuffer()
            for (byte in md5.digest()) {
                val toInt = (256 + byte.toInt()) and 255
                val hexString = Integer.toHexString(toInt)
                if (hexString.length < 2) sb.append("0")
                sb.append(hexString)
            }
            return sb.toString().toUpperCase()
        }

        @JvmStatic
        fun md5(text: String): String {
            val md5 = MessageDigest.getInstance("MD5")

            val buffer = text.toByteArray()
            md5.update(buffer)

            val sb = StringBuffer()
            for (byte in md5.digest()) {
                val toInt = (256 + byte.toInt()) and 255
                val hexString = Integer.toHexString(toInt)
                if (hexString.length < 2) sb.append("0")
                sb.append(hexString)
            }
            return sb.toString().toUpperCase()
        }
    }
}
