package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.Todos
import com.fdtheroes.sgruntbot.actions.persistence.TodosService
import org.springframework.stereotype.Service

@Service
class Todo(private val todosService: TodosService) : Action, HasHalp {

    private val regex_todo = Regex("^#TODO(\\w+\\d+)?\$", RegexOption.IGNORE_CASE)
    private val regex_todos = Regex("^#TODOS(\\w+ALL)?\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        val message = ctx.message
        if (regex_todo.matches(message.text)) {
            val quale = regex_todo.find(message.text)?.groupValues?.get(1)?.toLongOrNull()
            if (quale == null) {
                todosService.addTodo(message.from.id, message.text)
                ctx.addResponse(ActionResponse.message("Todo aggiunto. Datti da fare!"))
            } else {
                todosService.closeTodo(message.from.id, quale)
                ctx.addResponse(ActionResponse.message("Todo $quale cancellato. Bravo lavoratore!"))
            }
            ctx.addResponse(ActionResponse.message("Eh? "))
        }
        if (regex_todos.matches(message.text)) {
            val all = regex_todo.find(message.text)?.groupValues?.get(1)
            val todos: List<Todos>
            if (all.isNullOrEmpty()) {
                todos = todosService.allTodos(false)
            } else {
                todos = todosService.allTodos(true)
            }
            val risposta = todos.joinToString(separator = "\n") {
                val nr = it.id.toString().padStart(4)
                val chi = ctx.getChatMember(it.userId)?.userName.orEmpty().padEnd(8)
                val todo = it.todo.take(14) + "\\u2026"
                "$nr$chi$todo"
            }
            ctx.addResponse(ActionResponse.message(risposta))
        }
    }

    override fun halp(): String {
        return "<b>#TODO</b> aggiungi questo TODO / <b>#TODO (nr)</b> questo todo è terminato / <b>#TODOS ALL</b> tutti i todos / <b>#TODOS</b> i todos da fare"
    }
}
