package com.picpay.desafio.android.injector

import com.picpay.desafio.android.data.injector.Injector
import com.picpay.desafio.android.user.UserViewModelFactory

object ViewModelFactoryProvider {

    fun provideUserViewModelFactory(): UserViewModelFactory =
        UserViewModelFactory(Injector.provideUserRepository())
}