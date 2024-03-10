package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.Todos
import com.fdtheroes.sgruntbot.persistence.TodosService
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

@Service
class ScheduledTodos(private val botUtils: BotUtils, private val todosService: TodosService) : Scheduled {
    override fun firstRun() = mezzanotteVenerdiProssimo()

    override fun nextRun() = mezzanotteVenerdiProssimo()

    override fun execute() {
        val todosByUser = todosService.allTodos(true).groupBy { it.userId }
        val result = todosByUser.entries.joinToString(separator = "\n") {
            getTodoOfUser(it.key, it.value)
        }
        if (result.isNotEmpty()) {
            botUtils.messaggio(ActionResponse.message("<b>Utenti pigri con todos ancora aperti:</b>\n$result"))
        }
    }

    private fun getTodoOfUser(userId: Long, todos: List<Todos>): String {
        val utente = botUtils.getChatMember(userId)
        val userLink = botUtils.getUserLink(utente)
        val todosList = todos.joinToString(separator = "\n") {
            val nr = it.id.toString().padStart(4)
            val todo = it.todo
            "- $nr: $todo"
        }
        return "$userLink:\n$todosList"
    }

    private fun mezzanotteVenerdiProssimo(): LocalDateTime {
        val mezzanotte = LocalDateTime.now().withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return mezzanotte.with(TemporalAdjusters.next(DayOfWeek.SATURDAY)) // Mezzanotte tra venerdi e sabato
    }

}
