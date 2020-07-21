package com.picpay.desafio.android.common.mapper

/**
 * Interface to map service models to app models.
 */
interface DataMapper<T, R> {

    fun mapFrom(it: T): R
}