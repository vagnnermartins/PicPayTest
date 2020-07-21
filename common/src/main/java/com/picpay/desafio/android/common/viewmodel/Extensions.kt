package com.picpay.desafio.android.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, dispatcher: Dispatcher<T>) {
    observe(lifecycleOwner, Observer {
        dispatcher.dispatch(it)
    })
}