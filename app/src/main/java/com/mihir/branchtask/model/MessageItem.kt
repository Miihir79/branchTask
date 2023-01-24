package com.mihir.branchtask.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageItem(
    val agent_id: Int?,
    val body: String,
    val id: Int,
    val thread_id: Int,
    val timestamp: String,
    val user_id: String
):Parcelable