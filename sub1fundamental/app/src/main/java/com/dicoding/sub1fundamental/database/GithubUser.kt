package com.dicoding.sub1fundamental.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GithubUser(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var username: String = "",
    var avatarUrl: String? = null,

) : Parcelable