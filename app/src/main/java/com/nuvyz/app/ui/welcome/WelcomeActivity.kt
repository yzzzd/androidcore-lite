package com.nuvyz.app.ui.welcome

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.nuvyz.app.R
import com.nuvyz.app.base.activity.BaseActivity
import com.nuvyz.app.databinding.ActivityWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WelcomeActivity : BaseActivity<ActivityWelcomeBinding, WelcomeViewModel>(R.layout.activity_welcome) {

    override val viewModel by viewModels<WelcomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            delay(2_000)
            if (viewModel.isLogin()) {
                // openActivity<HomeActivity>()
            } else {
                // openActivity<LoginActivity>()
            }
            finish()
        }
    }
}