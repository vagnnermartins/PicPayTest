package com.picpay.desafio.android.data.injector

import com.picpay.desafio.android.common.cache.MemoryCache
import com.picpay.desafio.android.common.cache.RxCache
import com.picpay.desafio.android.data.mapper.DataMapperFactory
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.data.service.Gateway
import com.picpay.desafio.android.data.service.UserService
import io.reactivex.schedulers.Schedulers

object Injector {

    private lateinit var cache: RxCache<List<User>>

    fun provideUserRepository(): UserRepository {
        if (!Injector::cache.isInitialized) {
            cache = RxCache(MemoryCache())
        }
        return UserRepository(
            getUserService(),
            cache,
            DataMapperFactory(),
            Schedulers.io(),
            Schedulers.computation()
        )
    }

    private fun getUserService(): UserService {
        return Gateway.build().create(UserService::class.java)
    }
}