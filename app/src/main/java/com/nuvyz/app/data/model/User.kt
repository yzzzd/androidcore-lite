package com.nuvyz.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val idb: Int = 1,
    val id: String?,
    @SerializedName("salesman_id")
    val salesmanId: String?,
    val name: String?,
    val region: String?,
    val position: String?,
    val status: String?,
    val photo: String?
)
