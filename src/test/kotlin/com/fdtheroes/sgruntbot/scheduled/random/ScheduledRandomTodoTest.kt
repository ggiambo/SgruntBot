package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.Todos
import com.fdtheroes.sgruntbot.persistence.TodosService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class ScheduledRandomTodoTest : BaseTest() {

    @Test
    fun test_has_open_todo() {
        val scheduledRandomTodo = ScheduledRandomTodo(botUtils, todosServiceOneOpen())
        scheduledRandomTodo.execute()

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses[0].type).isEqualTo(ActionResponseType.Message)
        val (line1, line2) = actionResponses.first().message!!.split("\n")
        assertThat(line1).isEqualTo("Hei <a href=\"tg://user?id=3\">Username_3</a>, non hai ancora completato il todo: 'Todo 3'")
        assertThat(line2).isEqualTo("Muovi il culo, poi scrivi !TODO -42")
    }

    @Test
    fun test_no_open_todo() {
        val scheduledRandomTodo = ScheduledRandomTodo(botUtils, todosServiceNoOpen())
        scheduledRandomTodo.execute()

        assertThat(actionResponses).hasSize(0)
    }

    private fun todosServiceOneOpen() = mock<TodosService> {
        on { allTodos(true) } doReturn listOf(
            Todos(userId = 3, todo = "Todo 3", open = true, id = 42),
        )
    }

    private fun todosServiceNoOpen() = mock<TodosService> {
        on { allTodos(true) } doReturn emptyList()
    }


}
