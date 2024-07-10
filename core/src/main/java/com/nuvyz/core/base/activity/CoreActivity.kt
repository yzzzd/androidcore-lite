package com.nuvyz.core.base.activity

import android.content.Intent
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.nuvyz.core.base.fragment.CoreFragment
import com.nuvyz.core.utils.ActivityExtension.createImageFile
import com.nuvyz.core.utils.ActivityExtension.snackBarMsg
import com.nuvyz.core.utils.ActivityExtension.uriToFile
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.IOException

abstract class CoreActivity<VB: ViewDataBinding>(@LayoutRes private val layoutRes: Int): AppCompatActivity() {

    protected lateinit var binding: VB

    private var fragment: CoreFragment<*>? = null

    private var filePick: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // WindowCompat.setDecorFitsSystemWindows(window, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
    }

    protected fun addInset(view: View, left: Boolean = false, top: Boolean = false, right: Boolean = false, bottom: Boolean = false) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                if (left) leftMargin = insets.left
                if (top) topMargin = insets.top
                if (right) rightMargin = insets.right
                if (bottom) bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    protected fun snackBarMsg(message: String, duration: Int = Snackbar.LENGTH_LONG, onDismiss: (() -> Unit)? = null) {
        binding.root.snackBarMsg(message, duration, onDismiss)
    }

    val startActivityFResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        EventBus.getDefault().post(result)
    }

    val startIntentFResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        EventBus.getDefault().post(result)
    }

    private val pickResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (filePick != null) {
                EventBus.getDefault().post(filePick)
            } else {
                val fileUri = result.data?.data

                if (fileUri != null) {
                    val outputFile = uriToFile(fileUri, Environment.DIRECTORY_PICTURES)
                    EventBus.getDefault().post(outputFile)
                }
            }

        }
    }

    fun openGallery(fileType: String = "*/*") {
        val fileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = fileType
        }

        filePick = null
        pickResult.launch(fileIntent)
    }

    fun openCamera(front: Boolean = false) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (front) {
            when {
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT)  // Tested on API 24 Android version 7.0(Samsung S6)
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT) // Tested on API 27 Android version 8.0(Nexus 6P)
                    cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true)
                }
                else -> cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1)  // Tested API 21 Android version 5.0.1(Samsung S4)
            }
        }

        filePick = try {
            createImageFile()
        } catch (e: IOException) {
            // Error occurred while creating the File
            null
        } ?: return

        val photoURI = FileProvider.getUriForFile(this, "$packageName.file_provider", filePick ?: return)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        pickResult.launch(cameraIntent)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onResult(result: ActivityResult) {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}