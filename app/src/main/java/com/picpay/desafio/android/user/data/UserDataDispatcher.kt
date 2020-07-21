package com.picpay.desafio.android.user.data

import com.picpay.desafio.android.common.viewmodel.Dispatcher
import com.picpay.desafio.android.user.UserHandler

class UserDataDispatcher(private val handler: UserHandler) : Dispatcher<UserData> {

    override fun dispatch(item: UserData) = handler.bindData(item.users)
}