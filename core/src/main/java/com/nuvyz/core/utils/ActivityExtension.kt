package com.nuvyz.core.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.material.snackbar.Snackbar
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object ActivityExtension {
    inline fun <reified T : Activity> Context.openActivity(block: Intent.() -> Unit = {}) {
        startActivity(createIntent<T>(block))
    }

    inline fun <reified T : Activity> Context.createIntent(block: Intent.() -> Unit = {}): Intent {
        return Intent(this, T::class.java).apply(block)
    }

    inline fun <reified T> Intent.getParcelable(key: String): T? {
        setExtrasClassLoader(T::class.java.classLoader)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, T::class.java)
        } else {
            getParcelableExtra(key)
        }
    }

    inline fun <reified T> Intent.getParcelableList(key: String): List<T>? {
        setExtrasClassLoader(T::class.java.classLoader)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableArrayListExtra(key, T::class.java)
        } else {
            getParcelableArrayListExtra(key)
        }
    }

    fun Context.openMap(latitude: Double? = null, longitude: Double? = null, label: String? = null) {
        if (latitude != null && longitude != null) {
            val gmmIntentUri = if (label == null) {
                Uri.parse("geo:$latitude,$longitude")
            } else {
                Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(packageManager)?.let {
                startActivity(mapIntent)
            }
        } else {
            try {
                packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")?.let {
                    startActivity(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun Context.openWhatsApp(number: String) {
        val uri = Uri.parse("https://api.whatsapp.com/send/?phone=${number.removePrefix("+")}")
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(mapIntent)
    }

    fun Context.checkLocationEnabled(result: (Boolean, ResolvableApiException?) -> Unit) {
        val locationRequest = LocationRequest.Builder(1_000).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest).setAlwaysShow(true)

        val settingClient = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        settingClient.addOnCompleteListener {
            try {
                val response = settingClient.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location requests here.
                result(true, null)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the  user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = e as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            result(false, resolvable)
                            // resolvable.startResolutionForResult(this, Cons.REQ_GPS)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> result(true, null)
                }
            }
        }
    }

    fun Intent.getResult(name: Class<out Any>): Boolean {
        return getStringExtra("name") == name.simpleName
    }

    fun Intent.putResult(name: Class<out Any>) {
        putExtra("name", name.simpleName)
    }

    fun Context.openApp(url: Uri?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = url
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "No application found can handle this kind.", Toast.LENGTH_SHORT).show()
        }
    }

    fun View.snackBarMsg(message: String, duration: Int = Snackbar.LENGTH_LONG, onDismiss: (() -> Unit)? = null) {
        if (message.isEmpty()) return
        Snackbar.make(this, message, duration).apply {
            onDismiss?.let {
                addCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        it()
                    }
                })
            }
        }.show()
    }

    fun Context.uriToFile(uri: Uri, dir: String = Environment.DIRECTORY_PICTURES): File? {
        return try {
            val parcelFileDescriptor = uri.let { contentResolver.openFileDescriptor(it, "r") }
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val inputStream = FileInputStream(fileDescriptor)

            val primaryStorage = ContextCompat.getExternalFilesDirs(this, null)[0]

            val mediaDirectory = File(primaryStorage, dir)
            if (!mediaDirectory.exists()) {
                mediaDirectory.mkdir()
            }

            // val outputFileName = DateTimeHelper().createAt().replace(" ", "_") + "." + getFileExtension(uri)
            val outputFileName = getFileName(uri)
            val outputFile = File(mediaDirectory, outputFileName)
            val outputStream = FileOutputStream(outputFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            parcelFileDescriptor?.close()

            outputFile
        } catch (e: Exception) {
            null
        }
    }

    fun Context.getFileName(uri: Uri): String {
        // File Scheme.
        var fileName = "Untitled"
        if (ContentResolver.SCHEME_FILE == uri.scheme) {
            uri.path?.let {
                val file = File(it)
                fileName = file.name
                // file.length()
            }
        } else if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            val returnCursor = contentResolver.query(uri, null, null, null, null)
            if (returnCursor != null && returnCursor.moveToFirst()) {
                val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = returnCursor.getString(nameIndex)
                // val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
                // returnCursor.getLong(sizeIndex)
                returnCursor.close()
            }
        }
        return fileName
    }

    @Throws(IOException::class)
    fun Context.createImageFile(prefix: String = "JPEG_", suffix: String = ".jpg"): File {
        // Create an image file name
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(prefix, suffix, storageDir)
    }

    suspend fun Context.compressFileUpload(file: File, maxSize: Long = 262_144): File {
        return Compressor.compress(this, file) {
            resolution(816, 612)
            quality(100)
            format(Bitmap.CompressFormat.JPEG)
            size(maxSize) // 524_288
        }
    }
}