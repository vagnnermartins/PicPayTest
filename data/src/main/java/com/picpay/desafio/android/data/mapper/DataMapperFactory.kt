package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.common.mapper.DataMapper
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.data.service.model.UserResponse

class DataMapperFactory {

    fun userMapper(): DataMapper<List<UserResponse>, List<User>> = UserMapper()

}