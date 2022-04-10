package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.HasHalp
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/rest/sgrunt/v1")
class SgruntController(
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
    private val actions: List<Action>,
    private val karmaService: KarmaService,
) {

    @GetMapping("/actions")
    @Operation(summary = "Lista di tutte le azioni di Sgrunty")
    fun getActions(): List<Any> {
        return actions.map {
            object {
                val name = it::class.simpleName
                val halp = if (it is HasHalp) it.halp() else ""
            }
        }
    }

    @GetMapping("/lastAuthor")
    @Operation(summary = "Ultimo autore che ha scritto una boiata")
    fun getLastAuthor(): Any? {
        val lastAuthorId = Context.lastAuthor?.id
        if (lastAuthorId == null) {
            return null
        }
        return object {
            val userId = lastAuthorId
            val userName = botUtils.getUserName(sgruntBot.getChatMember(lastAuthorId))
        }
    }

    @GetMapping("/lastSuper")
    @Operation(summary = "Ultimo autore che ha fatto dire a Sgrunty una boiata")
    fun getLastSuper(): Any? {
        val lastSuperId = Context.lastSuper?.id
        if (lastSuperId == null) {
            return null
        }
        return object {
            val userId = lastSuperId
            val userName = botUtils.getUserName(sgruntBot.getChatMember(lastSuperId))
        }
    }

    @GetMapping("/pignolo")
    @Operation(summary = "Sgrunty è in modalità pignolo?")
    fun isPignolo(): Boolean {
        return Context.pignolo
    }

    @GetMapping("/pausedTime")
    @Operation(summary = "Fino a quando Sgrunty sta zitto")
    fun getPausedTime(): LocalDateTime? {
        return Context.pausedTime
    }

    @GetMapping("/randomScheduledActions")
    @Operation(summary = "Lista delle azioni schedulate")
    fun getRandomSchedules(): List<Any> {
        return Context.randomScheduled.map {
            val randomScheduledAction = it.key
            val delay = it.value
            object {
                val randomScheduledAction = randomScheduledAction.simpleName.orEmpty()
                val actionWhen = LocalDateTime.now().plus(delay)
            }
        }
    }

    @GetMapping("/karma")
    @Operation(summary = "Karma!")
    fun getKarma(): List<Any> {
        return karmaService.getKarmas()
            .map {
                object {
                    val userId = it.first
                    val userName = botUtils.getUserName(sgruntBot.getChatMember(it.first))
                    val karma = it.second
                }
            }
    }

}
