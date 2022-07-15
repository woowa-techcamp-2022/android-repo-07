package org.woowatechcamp.githubapplication.presentation.user.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val name : String,
    val nickname : String,
    val bio : String,
    val location : String,
    val blog : String,
    val email : String,
    val imgUrl : String,
    val followers: Int,
    val following: Int
) : Parcelable