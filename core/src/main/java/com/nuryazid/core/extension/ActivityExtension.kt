package com.nuryazid.core.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.WindowCompat

/**
 * Created by @yzzzd on 4/22/18.
 */

fun Context.tos(message: Int, short: Boolean = false) {
    tos(getString(message), short)
}

fun Context.tos(message: String, short: Boolean = false) {
    Toast.makeText(this, message, if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG).show()
}

inline fun <reified T : Activity> Context.openActivity(block: Intent.() -> Unit = {}) {
    startActivity(createIntent<T>(block))
}

inline fun <reified T : Activity> Context.openActivity(vararg pair: Pair<View, String>, block: Intent.() -> Unit = {}) {
    val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this as Activity, *pair).toBundle()
    startActivity(createIntent<T>(block), bundle)
}

inline fun <reified T : Activity> Context.createIntent(block: Intent.() -> Unit = {}): Intent {
    return Intent(this, T::class.java).apply(block)
}

/* make UI become fullscreen */
fun AppCompatActivity.setFullScreen(isStable: Boolean = false) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (!isStable) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    } else {
        if (isStable) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        }
    }

    /*WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, mainContainer).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }*/
}

/* check some permission has been granted */
fun Activity.allPermissionsGranted(permission: Array<String>): Boolean {
    permission.forEach {
        if (ContextCompat.checkSelfPermission(baseContext, it) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/* open the google maps app */
fun Activity.openMap() {
    try {
        packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")?.let {
            startActivity(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/* Open the dial activity */
fun Activity.openDial(phone: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone}")))
}