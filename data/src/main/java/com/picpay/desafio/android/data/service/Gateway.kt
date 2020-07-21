package com.picpay.desafio.android.data.service

import com.picpay.desafio.android.data.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Gateway {

    fun build(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BuildConfig.SERVER_URL)
        .client(UnsafeOkHttpClient.unsafeOkHttpClient)
        .build()
}