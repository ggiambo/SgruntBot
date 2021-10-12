import com.fdtheroes.sgruntbot.Bot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.File

fun main(args: Array<String>) {
    val token = File("token.txt").readText().trim()
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    botsApi.registerBot(Bot(token, "SgruntBot"))
}