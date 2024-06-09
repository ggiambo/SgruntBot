package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.TodosService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.math.abs

@Service
class Todo(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val todosService: TodosService,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex_todo_add = Regex("^!TODO (.+)\$", RegexOption.IGNORE_CASE)
    private val regex_todo_done = Regex("^!TODO (-\\d{1,6})\$", RegexOption.IGNORE_CASE)
    private val regex_todos = Regex("^!TODOS\$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val userId = message.from.id
        var risposta: String? = null
        when {
            regex_todo_done.matches(message.text) -> { // precedenza!
                val argomento = regex_todo_done.find(message.text)?.groupValues?.get(1)!!
                risposta = messaggioTodoChiuso(userId, abs(argomento.toLong()))
            }

            regex_todo_add.matches(message.text) -> {
                val argomento = regex_todo_add.find(message.text)?.groupValues?.get(1)!!
                risposta = messaggioTodoAggiunto(userId, argomento)
            }

            regex_todos.matches(message.text) -> {
                risposta = messaggioListaTodos()
            }
        }

        if (risposta != null) {
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    private fun messaggioTodoAggiunto(userId: Long, testoTodo: String): String {
        if (testoTodo.isBlank()) {
            return "Niente da fare..."
        }
        val todo = todosService.addTodo(userId, testoTodo)
        return "Todo '${todo.id}' aggiunto. Datti da fare!"
    }

    private fun messaggioTodoChiuso(userId: Long, todoId: Long): String {
        val closed = todosService.closeTodo(userId, todoId)
        if (closed) {
            return "Todo numero '$todoId' completato. Bravo lavoratore!"
        }
        return "Non posso chiudere il todo numero '$todoId'. Cosa combini?"
    }

    private fun messaggioListaTodos(): String {
        val todos = todosService.allTodos(true)
        return todos.joinToString(separator = "\n", prefix = "<pre>", postfix = "</pre>") {
            val nr = it.id.toString().padStart(4)
            val chi = botUtils.getUserName(botUtils.getChatMember(it.userId)).padStart(8)
            val todo = truncate(it.todo, 40)
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
        return "<b>!TODO</b> <i>testo</i> aggiungi un TODO / <b>!TODO -</b><i>nr</i> termina TODO con numero <i>nr</i> / <b>!TODOS</b> tutti i todos"
    }
}
