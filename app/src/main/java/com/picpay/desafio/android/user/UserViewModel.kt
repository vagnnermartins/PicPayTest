package com.picpay.desafio.android.user

import com.picpay.desafio.android.common.viewmodel.ActionViewModel
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.user.action.UserAction.*
import io.reactivex.Scheduler
import com.picpay.desafio.android.user.action.UserAction as Action
import com.picpay.desafio.android.user.data.UserData as Data

class UserViewModel(
    private val repository: UserRepository,
    private val uiScheduler: Scheduler
) : ActionViewModel<Action, Data>() {

    init {
        load()
    }

    fun load() {
        disposeOnClear {
            dispatchAction(ShowProgress)
            repository.getUsers()
                .observeOn(uiScheduler)
                .subscribe({ dispatchData(Data(it)) }, { dispatchAction(ShowError) })
        }
    }

    fun goToUserDetailClicked(userId: Long) = dispatchAction(GoToUserDetail(userId))
}