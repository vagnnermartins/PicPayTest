package com.picpay.desafio.android.user.action

sealed class UserAction {

    data class GoToUserDetail(val userId: Long) : UserAction()
    object ShowProgress : UserAction()
    object ShowError : UserAction()
}