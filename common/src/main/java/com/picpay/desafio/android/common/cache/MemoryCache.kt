package com.picpay.desafio.android.common.cache

open class MemoryCache<T> : Cache<T> {

    private val map = HashMap<String,T>()

    override fun save(key: String, value: T) {
        map[key] = value
    }

    override fun get(key: String) = map[key]

    override fun clear(key: String) {
        map.remove(key)
    }

    override fun clear() {
        map.clear()
    }
}
