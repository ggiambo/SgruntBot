import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main(args: Array<String>) {
    val config = BotConfig(args)
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    botsApi.registerBot(Bot(config))
}