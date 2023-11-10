package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.TodosService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.math.abs

@Service
class Todo(private val todosService: TodosService) : Action, HasHalp {

    private val regex_todo_add = Regex("^!TODO (.+)\$", RegexOption.IGNORE_CASE)
    private val regex_todo_done = Regex("^!TODO (-\\d+)\$", RegexOption.IGNORE_CASE)
    private val regex_todos = Regex("^!TODOS\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        val message = ctx.message
        val userId = ctx.message.from.id
        var risposta: String? = null
        if (regex_todo_done.matches(message.text)) { // precedenza!
            val argomento = regex_todo_done.find(message.text)?.groupValues?.get(1)
            risposta = messaggioTodoChiuso(userId, argomento.orEmpty())
        } else if (regex_todo_add.matches(message.text)) {
            val argomento = regex_todo_add.find(message.text)?.groupValues?.get(1)
            risposta = messaggioTodoAggiunto(userId, argomento.orEmpty())
        } else if (regex_todos.matches(message.text)) {
            risposta = messaggioListaTodos(ctx.getChatMember)
        }

        if (risposta != null) {
            ctx.addResponse(ActionResponse.message(risposta))
        }
    }

    private fun messaggioTodoAggiunto(userId: Long, testoTodo: String): String {
        val todo = todosService.addTodo(userId, testoTodo)
        return "Todo '${todo.id}' aggiunto. Datti da fare!"
    }

    private fun messaggioTodoChiuso(userId: Long, argomento: String): String {
        val todoId = argomento.toLongOrNull().let {
            if (it != null) {
                abs(it) // c'è un "-" davanti al numero!
            } else {
                null
            }
        }
        if (todoId == null) {
            return "Non riesco a trovare il TODO '$argomento'"
        }
        val closed = todosService.closeTodo(userId, todoId)
        if (closed) {
            return "Todo numero '$todoId' completato. Bravo lavoratore!"
        }
        return "Non posso chiudere il todo numero '$todoId'. Cosa combini?"
    }

    private fun messaggioListaTodos(getChatMember: (Long) -> User?): String {
        val todos = todosService.allTodos(true)
        return todos.joinToString(separator = "\n", prefix = "<pre>", postfix = "</pre>") {
            val nr = it.id.toString().padStart(4)
            val chi = getChatMember(it.userId)?.userName.orEmpty().padStart(8)
            val todo = truncate(it.todo, 14)
            "$nr $chi '$todo'"
        }
    }

    private fun truncate(message: String, length: Int): String {
        if (message.length < length) {
            return message
        }
        if (length < 1) {
            return message
        }
        return message.take(length - 1) + '\u2026'
    }

    override fun halp(): String {
        return "<b>!TODO testo</b> aggiungi un TODO / <b>!TODO -nr</b> questo todo è terminato / <b>!TODOS</b> tutti i todos"
    }
}
