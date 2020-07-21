package com.picpay.desafio.android.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.common.tests.TestObserver
import com.picpay.desafio.android.data.repository.UserRepository
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.user.action.UserAction
import com.picpay.desafio.android.user.action.UserAction.GoToUserDetail
import com.picpay.desafio.android.user.data.UserData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val repository = mockk<UserRepository>(relaxed = true)

    private lateinit var viewModel: UserViewModel

    private val users = mockk<List<User>>()

    @Before
    fun setUp() {
        every { repository.getUsers() } answers { Single.just(users) }

        viewModel = UserViewModel(repository, Schedulers.trampoline())
    }

    @Test
    fun `On init, users is fetched`() {
        val testObserver = TestObserver<UserData>()

        viewModel.data.observeForever(testObserver)

        verify { repository.getUsers() }
        assertEquals(UserData(users), testObserver.lastValue())
    }

    @Test
    fun `On spotlight detail clicked, go to spotlight detail is emitted`() {
        val userId = 1001L
        val testObserver = TestObserver<UserAction>()

        viewModel.actions.observeForever(testObserver)

        viewModel.goToUserDetailClicked(userId)

        assertEquals(GoToUserDetail(userId), testObserver.lastValue())
    }
}