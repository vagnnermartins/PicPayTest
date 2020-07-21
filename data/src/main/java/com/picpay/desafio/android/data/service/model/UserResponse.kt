package com.picpay.desafio.android.data.service.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Long,
    val name: String,
    @SerializedName("img") val image: String,
    val username: String
)