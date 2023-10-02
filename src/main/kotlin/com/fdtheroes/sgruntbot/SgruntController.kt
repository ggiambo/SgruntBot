package com.fdtheroes.sgruntbot


import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.HasHalp
import com.fdtheroes.sgruntbot.actions.models.Karma
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import com.fdtheroes.sgruntbot.scheduled.InitScheduled
import io.swagger.v3.oas.annotations.Operation
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/sgrunty/rest")
class SgruntController(
    private val sgruntBot: Bot,
    private val botUtils: BotUtils,
    private val actions: List<Action>,
    private val karmaService: KarmaService,
    private val statsService: StatsService,
    private val botConfig: BotConfig,
    private val initScheduled: InitScheduled,
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
        val lastAuthorId = botConfig.lastAuthor?.id
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
        val lastSuperId = botConfig.lastSuper?.id
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
        return botConfig.pignolo
    }

    @GetMapping("/pausedTime")
    @Operation(summary = "Fino a quando Sgrunty sta zitto")
    fun getPausedTime(): LocalDateTime? {
        return botConfig.pausedTime
    }

    @GetMapping("/karma")
    @Operation(summary = "Karma!")
    fun getKarma(): List<Karma> {
        return karmaService.getKarmas().toList()
    }

    @GetMapping("/stats/{date}")
    @Operation(summary = "Statistiche a partire da una certa data (YYYY-MM-DD)")
    fun getKarma(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate = LocalDate.now()): List<Any> {
        return statsService.getStatsFromDate(date)
            .map {
                object {
                    val userId = it.userId
                    val userName = botUtils.getUserName(sgruntBot.getChatMember(it.userId))
                    var day = it.statDay
                    var messages = it.messages
                }
            }
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Cosa ci riseva nel futuro Sgrunty?")
    fun getScheduled(): List<Any> {
        return initScheduled.getSchedulingInfo()
            .map {
                object {
                    val cosa = it.key.simpleName
                    val quando = it.value
                }
            }
    }

}
