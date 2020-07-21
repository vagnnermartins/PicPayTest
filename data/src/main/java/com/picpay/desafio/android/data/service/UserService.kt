package com.picpay.desafio.android.data.service

import com.picpay.desafio.android.data.service.model.UserResponse
import io.reactivex.Single
import retrofit2.http.GET


interface UserService {

    @GET("users")
    fun getUsers(): Single<List<UserResponse>>
}