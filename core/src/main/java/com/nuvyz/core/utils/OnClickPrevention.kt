package com.nuvyz.core.utils

import android.view.View

interface OnClickPrevention: View.OnClickListener {

    override fun onClick(v: View?) {
        preventClickTwice(v)
    }
    fun preventClickTwice(view: View?) {
        view?.isEnabled = false
        view?.postDelayed({ view.isEnabled = true }, 1_500L)
    }
}