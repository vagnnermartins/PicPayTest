package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.common.cache.RxCache
import com.picpay.desafio.android.data.mapper.DataMapperFactory
import com.picpay.desafio.android.data.mapper.UserMapper
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.data.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private val service = mockk<UserService>(relaxed = true)
    private val cache = mockk<RxCache<List<User>>>(relaxed = true)
    private val dataMapperFactory = mockk<DataMapperFactory>(relaxed = true)
    private val productsMapper = mockk<UserMapper>(relaxed = true)

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        every { dataMapperFactory.userMapper() } returns productsMapper
        repository = UserRepository(
            service,
            cache,
            dataMapperFactory,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Test
    fun `Get Products attempts to get cached data`() {
        repository.getUsers().test()

        verify { cache.get(PIC_PAY_GET_USER_KEY, any(), forceLoad = false, skipErrors = false) }
    }

    @Test
    fun `Get Produtcs uses service as data source`() {
        val sourceSlot = slot<Single<List<User>>>()

        every {
            cache.get(
                PIC_PAY_GET_USER_KEY,
                capture(sourceSlot),
                forceLoad = false,
                skipErrors = false
            )
        } returns mockk(relaxed = true)

        repository.getUsers().test()

        verify { cache.get(PIC_PAY_GET_USER_KEY, any(), forceLoad = false, skipErrors = false) }

        sourceSlot.captured.test()
        verify { service.getUsers() }
    }

    @Test
    fun `Get Products emits error when failing to get products`() {
        val exception = RuntimeException("Failed to get products")
        every {
            cache.get(
                PIC_PAY_GET_USER_KEY,
                any(),
                forceLoad = false,
                skipErrors = false
            )
        } returns Flowable.error(exception)

        val testObserver = repository.getUsers().test()

        testObserver.assertError(exception)
    }
}