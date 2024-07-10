package com.nuvyz.app.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.nuvyz.app.base.dao.BaseDao
import com.nuvyz.app.data.model.User

@Dao
interface UserDao: BaseDao<User> {

    @Query("SELECT * FROM User WHERE idb = 1")
    suspend fun getLoginUser(): User?

    @Query("DELETE FROM User")
    suspend fun deleteLoginUser()
}