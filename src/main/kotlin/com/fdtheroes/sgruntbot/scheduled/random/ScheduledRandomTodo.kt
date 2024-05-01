package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.TodosService
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class ScheduledRandomTodo(private val botUtils: BotUtils, private val todosService: TodosService) : Scheduled {
    override fun execute() {
        val todos = todosService.allTodos(true)
        if (todos.isEmpty()) {
            return
        }

        val todo = todos.random()
        val user = botUtils.getChatMember(todo.userId)
        if (user == null) {
            return
        }

        val testo1 = "Hei ${botUtils.getUserLink(user)}, non hai ancora completato il todo: '${todo.todo}'"
        val testo2 = "Muovi il culo, poi scrivi !TODO -${todo.id}"

        botUtils.messaggio(ActionResponse.message("$testo1\n$testo2"))
    }

    // random tra 24 e 36 ore
    override fun firstRun(): LocalDateTime {
        val ore = Random.nextLong(24, 36)
        return LocalDateTime.now().plusHours(ore)
    }

    // random tra 24 e 36 ore
    override fun nextRun() = firstRun()

}
