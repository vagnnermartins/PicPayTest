package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.common.cache.RxCache
import com.picpay.desafio.android.data.mapper.DataMapperFactory
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.data.service.UserService
import io.reactivex.Scheduler
import io.reactivex.Single

const val PIC_PAY_GET_USER_KEY = "PIC_PAY_GET_USER_KEY"

class UserRepository(
    private val service: UserService,
    private val cache: RxCache<List<User>>,
    private val dataMapperFactory: DataMapperFactory,
    private val ioScheduler: Scheduler,
    private val computationScheduler: Scheduler
) {

    /**
     * Gets the Users from the [cache] if there is a cached version and makes a network request
     * to get the products otherwise.
     *
     * @return [Single] that completes when successfully getting the products or errors if the campaign can't be retrieved.
     */
    fun getUsers(): Single<List<User>> {
        return cache.get(
            PIC_PAY_GET_USER_KEY,
            service.getUsers().observeOn(computationScheduler)
                .map { dataMapperFactory.userMapper().mapFrom(it) },
            skipErrors = false
        )
            .firstOrError()
            .subscribeOn(ioScheduler)
    }
}