package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.Todos
import com.fdtheroes.sgruntbot.actions.persistence.TodosService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDateTime

class TestScheduledTodos : BaseTest() {

    @Test
    fun testFirstRun() {
        val scheduledTodos = ScheduledTodos(sgruntBot, botUtils, mock())
        val nextRun = scheduledTodos.firstRun()
        val now = LocalDateTime.now()

        assertThat(nextRun).isAfter(now)
    }

    @Test
    fun testNextRun() {
        val scheduledTodos = ScheduledTodos(sgruntBot, botUtils, mock())
        val nextRun = scheduledTodos.nextRun()
        val now = LocalDateTime.now()

        assertThat(nextRun).isAfter(now)
    }

    @Test
    fun testExecute_noMessages() {
        val scheduledTodos = ScheduledTodos(sgruntBot, botUtils, mock())
        scheduledTodos.execute()

        verify(sgruntBot, times(0)).messaggio(isA())
    }

    @Test
    fun testExecute_messages() {
        val todosService = mock<TodosService> {
            on { allTodos(isA()) } doReturn listOf(
                Todos(userId = 99, todo = "Todo id 9 Utente 99", open = true, id = 9),
                Todos(userId = 42, todo = "Todo id 1 Utente 42", open = true, id = 1),
                Todos(userId = 99, todo = "Todo id 7 Utente 99", open = true, id = 7),
                Todos(userId = 99, todo = "Todo id 3 Utente 99", open = true, id = 3),
                Todos(userId = 42, todo = "Todo id 5 Utente 42", open = true, id = 5),
            )
        }
        val scheduledTodos = ScheduledTodos(sgruntBot, botUtils, todosService)
        scheduledTodos.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        assertThat(argumentCaptor.allValues).hasSize(1)
        val message = argumentCaptor.firstValue.message!!
        assertThat(message).startsWith("<b>Utenti pigri con todos ancora aperti:</b>")
        val righe = message.split("\n")
        assertThat(righe).containsOnly(
            "<b>Utenti pigri con todos ancora aperti:</b>",
            "<a href=\"tg://user?id=99\">Username_99</a>:",
            "-    9: Todo id 9 Utente 99",
            "-    7: Todo id 7 Utente 99",
            "-    3: Todo id 3 Utente 99",
            "<a href=\"tg://user?id=42\">Username_42</a>:",
            "-    1: Todo id 1 Utente 42",
            "-    5: Todo id 5 Utente 42",
        )
    }
}
