package org.woowatechcamp.githubapplication.data.issue.model

import com.google.gson.annotations.SerializedName

data class Label(
    val color: String,
    val default: Boolean,
    val description: String,
    val id: Long,
    val name: String,
    @SerializedName("node_id") val nodeId: String,
    val url: String
)