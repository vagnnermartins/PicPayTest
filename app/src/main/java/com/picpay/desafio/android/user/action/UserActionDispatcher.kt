package com.picpay.desafio.android.user.action

import com.picpay.desafio.android.common.viewmodel.Dispatcher
import com.picpay.desafio.android.user.UserHandler

class UserActionDispatcher(private val handler: UserHandler) : Dispatcher<UserAction> {

    override fun dispatch(item: UserAction) = when (item) {
        is UserAction.GoToUserDetail -> handler.goToUserDetailScreen(item.userId)
        UserAction.ShowProgress -> handler.showProgress()
        UserAction.ShowError -> handler.showError()
    }
}