package com.picpay.desafio.android.user.data

import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.user.UserHandler
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class UserDataDispatcherTest {

    private val handler = mockk<UserHandler>(relaxed = true)
    private val dispatcher = UserDataDispatcher(handler)

    @Test
    fun `Bind Data is dispatched`() {
        val users = mockk<List<User>>()
        dispatcher.dispatch(UserData(users))

        verify { handler.bindData(users) }
    }
}