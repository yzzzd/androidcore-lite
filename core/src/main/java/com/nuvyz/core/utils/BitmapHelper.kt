package com.nuvyz.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object BitmapHelper {
    fun Bitmap.toBase64(): String {
        var baos = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 100, baos)
        var b = baos.toByteArray()
        var temp: String? = null
        try {
            System.gc()
            temp = Base64.encodeToString(b, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            baos = ByteArrayOutputStream()
            compress(Bitmap.CompressFormat.JPEG, 50, baos)
            b = baos.toByteArray()
            temp = Base64.encodeToString(b, Base64.DEFAULT)
        }
        return temp ?: "null"
    }

    fun Context.decodeBase64(input: String): Bitmap {
        val decodedByte = Base64.decode(input, 0)

        val sdCardDirectory = File(cacheDir, "")

        val nw = "IMG_${DateFormatter.timeNow()}.jpg"
        val image = File(sdCardDirectory, nw)

        // Encode the file as a PNG image.
        var outStream: FileOutputStream? = null
        try {
            outStream = FileOutputStream(image)
            outStream.write(input.toByteArray())
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return try {
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun File.toBase64(): String {
        return BitmapFactory.decodeFile(absolutePath).toBase64()
    }
}