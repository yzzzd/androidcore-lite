package com.nuvyz.app.ui.welcome

import androidx.lifecycle.ViewModel
import com.nuvyz.app.data.source.local.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {
    suspend fun isLogin() = userDao.getLoginUser() != null
}