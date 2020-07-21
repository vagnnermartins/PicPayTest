package com.picpay.desafio.android.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base view model that is intended to consume data using RX and exposes events through two different [LiveData]s: actions and data.
 *
 * The [actions] live data will emit one shot events like reacting to click events. The [data] live data should represent all the data needed to
 * bind the UI.
 *
 * The [actions] and the [data] are separated so when re-observing the live data (e.g. activity recreate) the UI can be recreated and the latest
 * actions (like clicks) won't be emitted again causing wrong behaviors.
 *
 * @see [Dispatcher]
 */
abstract class ActionViewModel<Action, Data> : ViewModel() {

    private val actionsMutableLiveData = SingleLiveEvent<Action>()
    private val dataMutableLiveData = MutableLiveData<Data>()
    private val disposableContainer = CompositeDisposable()

    val actions: LiveData<Action> = actionsMutableLiveData
    val data: LiveData<Data> = dataMutableLiveData

    override fun onCleared() {
        disposableContainer.dispose()
        super.onCleared()
    }

    fun dispatchAction(action: Action) {
        actionsMutableLiveData.value = action
    }

    fun dispatchData(data: Data) {
        dataMutableLiveData.value = data
    }

    fun disposeOnClear(disposable: () -> Disposable) {
        disposableContainer.add(disposable())
    }
}
