package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.BotUtils.Companion.length
import com.fdtheroes.sgruntbot.actions.models.Todos
import com.fdtheroes.sgruntbot.actions.persistence.TodosService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TodoTest : BaseTest() {

    private val todosService = mock<TodosService> {
        on { addTodo(isA(), isA()) } doReturn Todos(
            userId = 42,
            todo = "dummy",
            open = true,
            id = 99
        )
        on { closeTodo(isA(), isA()) } doAnswer {
            it.arguments[1] == 42L
        }
        on { allTodos(isA()) } doReturn listOf(
            Todos(
                userId = 1,
                todo = "Todo corto",
                open = true,
                id = 111
            ),
            Todos(
                userId = 2,
                todo = "Todo con testo molto lungo che spero venga troncato correttamente",
                open = true,
                id = 222
            ),
        )
    }

    private val todo = Todo(todosService, botUtils)

    @Test
    fun test_no_trigger() {
        val actionContext = actionContext(text = "!TODO")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(0)
    }

    @Test
    fun test_todo_vuoto() {
        val actionContext = actionContext(text = "!TODO  \t     \t")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(1)
        assertThat(actionContext.actionResponses.first().message).isEqualTo("Niente da fare...")
    }

    @Test
    fun test_aggiunge_todo() {
        val actionContext = actionContext(text = "!TODO fai qualcosa")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(1)
        assertThat(actionContext.actionResponses.first().message).isEqualTo("Todo '99' aggiunto. Datti da fare!")
        val addTodoCaptor = argumentCaptor<Long, String>()
        verify(todosService, times(1)).addTodo(addTodoCaptor.first.capture(), addTodoCaptor.second.capture())
        assertThat(addTodoCaptor.first.firstValue).isEqualTo(42)
        assertThat(addTodoCaptor.second.firstValue).isEqualTo("fai qualcosa")
    }


    @Test
    fun test_cancellare_todo_missing() {
        val actionContext = actionContext(text = "!TODO -123")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(1)
        assertThat(actionContext.actionResponses.first().message).isEqualTo("Non posso chiudere il todo numero '123'. Cosa combini?")
        verify(todosService, times(0)).addTodo(any(), any())
    }

    @Test
    fun test_cancellare_todo() {
        val actionContext = actionContext(text = "!TODO -42")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(1)
        assertThat(actionContext.actionResponses.first().message).isEqualTo("Todo numero '42' completato. Bravo lavoratore!")
    }

    @Test
    fun test_list_todo() {
        val actionContext = actionContext(text = "!TODOS")
        todo.doAction(actionContext)

        assertThat(actionContext.actionResponses).hasSize(1)
        assertThat(actionContext.actionResponses.first().message).isNotNull()
        val lines = actionContext.actionResponses.first().message!!.split("\n")
        assertThat(lines.length()).isEqualTo(2)
        assertThat(lines[0]).isEqualTo("<pre> 111 Username_1 'Todo corto'")
        assertThat(lines[1]).isEqualTo(" 222 Username_2 'Todo con testo molto lungo che spero ve…'</pre>")
    }
}