package com.picpay.desafio.android.user

import com.picpay.desafio.android.data.repository.model.User

interface UserHandler {

    fun goToUserDetailScreen(userId: Long)
    fun bindData(data: List<User>)
    fun showProgress()
    fun showError()
}