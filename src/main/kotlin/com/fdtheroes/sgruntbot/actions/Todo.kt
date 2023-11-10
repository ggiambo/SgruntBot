package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.Todos
import com.fdtheroes.sgruntbot.actions.persistence.TodosService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

@Service
class Todo(private val todosService: TodosService) : Action, HasHalp {

    private val regex_todo = Regex("^!TODO([+-]) (.+)\$", RegexOption.IGNORE_CASE)
    private val regex_todos = Regex("^!TODOS\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        val message = ctx.message
        val userId = ctx.message.from.id
        if (regex_todo.matches(message.text)) {
            messaggioTodo(userId, message)
        } else if (regex_todos.matches(message.text)) {
            messaggioListaTodos(message, ctx.getChatMember)
        }
    }

    private fun messaggioTodo(userId: Long, message: Message): String {
        val operazione = regex_todo.find(message.text)!!.groupValues[1]
        val valore = regex_todo.find(message.text)!!.groupValues[2]
        if (operazione == "+") {
            return messaggioTodoAggiunto(userId, valore)
        }
        return messaggioTodoChiuso(userId, valore)
    }

    private fun messaggioTodoAggiunto(userId: Long, valore: String): String {
        todosService.addTodo(userId, valore)
        return "Todo aggiunto. Datti da fare!"
    }

    private fun messaggioTodoChiuso(userId: Long, valore: String): String {
        val todoId = valore.toLongOrNull()
        if (todoId == null) {
            return "Non riesco a trovare il TODO numero '$todoId'"
        }
        val closed = todosService.closeTodo(userId, todoId)
        if (closed) {
            return "Todo numero '$todoId' completato. Bravo lavoratore!"
        }
        return "Non posso chiudere il todo numero '$todoId'. Cosa combini?"
    }

    private fun messaggioListaTodos(message: Message, getChatMember: (Long) -> User?): String {
        val all = regex_todo.find(message.text)?.groupValues?.get(1)
        val todos: List<Todos>
        if (all.isNullOrEmpty()) {
            todos = todosService.allTodos(false)
        } else {
            todos = todosService.allTodos(true)
        }
        return todos.joinToString(separator = "\n") {
            val nr = it.id.toString().padStart(4)
            val chi = getChatMember(it.userId)?.userName.orEmpty().padEnd(8)
            val todo = it.todo.take(14) + "\\u2026"
            "$nr$chi$todo"
        }
    }

    override fun halp(): String {
        return "<b>!TODO+</b> aggiungi un TODO / <b>#TODO- (nr)</b> questo todo è terminato / <b>!TODOS</b> tutti i todos"
    }
}
