package com.picpay.desafio.android.common.viewmodel

/**
 * Interface that observes live data and dispatches the events
 *
 * @see [ActionViewModel]
 */
interface Dispatcher<T> {

    fun dispatch(item: T)
}