package com.picpay.desafio.android.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.picpay.desafio.android.data.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(repository, AndroidSchedulers.mainThread()) as T
    }

}