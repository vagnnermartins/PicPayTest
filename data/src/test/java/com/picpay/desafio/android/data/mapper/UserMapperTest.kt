package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.data.service.model.UserResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class UserMapperTest {

    private val dataMapper = UserMapper()

    @Test
    fun `Assert List User Mapper`() {
        val id = 1001L
        val name = "image"
        val image = "image"
        val username = "username"

        val gw = mockk<UserResponse> {
            every { this@mockk.id } returns id
            every { this@mockk.name } returns name
            every { this@mockk.image } returns image
            every { this@mockk.username } returns username
        }

        val modelUsers = dataMapper.mapFrom(listOf(gw))

        assertEquals(gw.id, modelUsers[0].id)
        assertEquals(gw.name, modelUsers[0].name)
        assertEquals(gw.image, modelUsers[0].image)
        assertEquals(gw.username, modelUsers[0].username)
    }
}