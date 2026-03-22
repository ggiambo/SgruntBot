package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepositoryHelper
import com.fdtheroes.sgruntbot.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class ChiEraTest : BaseTest() {

    private val usersService = mock<UsersService>()

    private val nameValuePairRepositoryHelper = NameValuePairRepositoryHelper()
    private val chiEra =
        ChiEra(usersService, nameValuePairRepositoryHelper.nameValuePairRepository(), botUtils, botConfig)

    @BeforeEach
    fun init() {
        nameValuePairRepositoryHelper.reset()
    }

    @Test
    fun testPositive() {
        val user = user(userName = "Username_42")
        val lastSuper = NameValuePair(NameValuePair.NameValuePairName.LAST_SUPER, user.id.toString())
        nameValuePairRepositoryHelper.nameValuePairRepository().save(lastSuper)
        whenever(usersService.getAllUsers()).doReturn(listOf(user))
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("""<a href="tg://user?id=42">Username_42</a>""")
    }

    @Test
    fun testNegative() {
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}
