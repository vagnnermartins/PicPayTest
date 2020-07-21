package com.picpay.desafio.android

import retrofit2.Call
import retrofit2.http.GET

@Deprecated("Initial class with the presented problems to be solved. Use [com.picpay.desafio.android.data.service.UserService]")
interface PicPayService {

    @GET("users")
    fun getUsers(): Call<List<User>>
}