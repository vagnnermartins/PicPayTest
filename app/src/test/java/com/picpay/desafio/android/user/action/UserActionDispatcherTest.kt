package com.picpay.desafio.android.user.action

import com.picpay.desafio.android.user.UserHandler
import com.picpay.desafio.android.user.action.UserAction.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class UserActionDispatcherTest {

    private val handler = mockk<UserHandler>(relaxed = true)
    private val dispatcher = UserActionDispatcher(handler)

    @Test
    fun `Go to user detail is handled when go to user detail action is dispatched`() {
        val userId = 1001L

        dispatcher.dispatch(GoToUserDetail(userId))

        verify { handler.goToUserDetailScreen(userId) }
    }

    @Test
    fun `Show progress is handled when show progress action is dispatched`() {
        dispatcher.dispatch((ShowProgress))

        verify { handler.showProgress() }
    }

    @Test
    fun `Show error is handled when show error action is dispatched`() {
        dispatcher.dispatch((ShowError))

        verify { handler.showError() }
    }
}