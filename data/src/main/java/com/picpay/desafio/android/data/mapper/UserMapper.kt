package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.common.mapper.DataMapper
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.data.service.model.UserResponse

class UserMapper : DataMapper<List<UserResponse>, List<User>> {

    override fun mapFrom(it: List<UserResponse>): List<User> =
        it.map { User(it.id, it.name, it.image, it.username) }
}