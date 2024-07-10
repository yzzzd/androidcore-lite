package com.nuvyz.core.utils

import android.widget.ImageView
import coil.load
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.File

object ImagePreviewHelper {
    fun show(imageView: ImageView, image: String?) {
        if (image.isNullOrEmpty()) return
        StfalconImageViewer.Builder(imageView.context, arrayOf(image)) { view, url ->
            if (url.isUrl()) {
                view.load(url)
            } else {
                view.load(File(url))
            }
        }.withTransitionFrom(imageView).show(true)
    }

    fun show(imageView: ImageView, images: List<String?>?, position: Int = 0) {
        if (images.isNullOrEmpty()) return
        StfalconImageViewer.Builder(imageView.context, images) { view, url ->
            if (url?.isUrl() == true) {
                view.load(url)
            } else {
                view.load(File(url ?: return@Builder))
            }
        }.withTransitionFrom(imageView).show(true).setCurrentPosition(position)
    }

    private fun String.isUrl(): Boolean {
        return contains("http", true)
    }
}