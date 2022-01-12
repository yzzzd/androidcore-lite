package com.nuryazid.core.extension

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created by @yzzzd on 4/22/18.
 */

fun File.compress(context: Context, prefWidth: Int, prefHeight: Int, prefSize: Long, result: (file: File) -> Unit) = CoroutineScope(Dispatchers.IO).launch {
    result(
        Compressor.compress(context, this@compress) {
            resolution(prefWidth, prefHeight)
            quality(100)
            size(prefSize)
        }
    )
}