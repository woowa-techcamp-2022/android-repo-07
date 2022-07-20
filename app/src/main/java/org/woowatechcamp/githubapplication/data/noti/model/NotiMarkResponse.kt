package org.woowatechcamp.githubapplication.data.notifications.model

import com.google.gson.annotations.SerializedName

data class NotiMarkResponse(
    @SerializedName("Status") val status: Int
)