package com.picpay.desafio.android.common.cache

interface Cache<T> {
    fun save(key: String, value: T)
    fun get(key: String) : T?
    fun clear(key: String)
    fun clear()
}