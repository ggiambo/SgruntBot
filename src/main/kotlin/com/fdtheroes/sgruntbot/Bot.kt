package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.utils.*
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random


class Bot(private val botToken: String, private val botUsername: String) : TelegramLongPollingBot() {

    private var pausedTime: LocalDateTime? = null
    private var lastAuthor: String? = null
    private var lastSuper: Message? = null

    private val getUserLink = UserUtils()::getUserLink
    private val botRegex = BotRegex()
    private var rispondi = RispondiUtils()::rispondi
    private val rispondiAsText = RispondiUtils()::rispondiAsText
    private val oreInLettere = OreUtils()::oreInLettere
    private val aesEncrypt = AESUtils()::aesEncrypt
    private val aesDecrypt = AESUtils()::aesDecrypt
    private val slogan = SloganUtils()::slogan
    private val bitcoinvalue = MoneyUtils()::bitcoinvalue
    private val canzo = CanzoUtils()::canzo
    private val bullshitInEuro = MoneyUtils()::bullshitInEuro

    init {
        execute(SendMessage("32657811", "Sono partito"))
    }

    override fun getBotToken(): String {
        return botToken
    }

    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update?) {
        if (pausedTime != null) {
            if (ChronoUnit.MINUTES.between(pausedTime, LocalDateTime.now()) > 5) {
                pausedTime = null
            } else {
                return
            }
        }

        val message = update?.message
        if (message == null) {
            return
        }

        println("Messaggio da: ${getUserLink(message)}")
        println(message.text)

        if (!botRegex.last(message)) {
            storeLastAuthor(message)
        }

        val pignolo = Random.nextInt(100) > 90
        if (message.text == "!test") {
            rispondi(this, message, "${getUserLink(message)}: toast `test`")
        } else if (botRegex.parlaSuper(message) != null && setOf<Long>(
                32657811,
                353708759,
                252800958,
                250965179,
                68714652,
                259607683,
                104278889
            ).contains(message.from.id)
        ) {
            executeAsync(SendMessage("-1001103213994", "${botRegex.parlaSuper(message)}"))
            lastSuper = message
        } else if (botRegex.chiEra(message)) {
            rispondi(this, message, getUserLink(lastSuper))
        } else if (botRegex.parolacceMatch(message) && pignolo) {
            val msgs = listOf(
                "${getUserLink(message)} non approvo il tuo linguaggio, tuttavia in uno sforzo congiunto nella direzione del benessere comune non sarò io a dirti cosa dire o meno, ma storcerò soltanto il naso.",
                "${getUserLink(message)} non dire parolacce!",
                "Ma dai ${getUserLink(message)}, ci sono dei bambini!"
            )
            val msg = msgs[Random.nextInt(2)]
            rispondi(this, message, msg)
        } else if (pignolo && Regex("porc[oa] ?d+io", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, "E la madooonna!")
        } else if (pignolo && Regex("dio ?(porco|cane)", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, "Che mi tocca sentire!")
        } else if (pignolo && Regex("porca ?madonna", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, "...e tutti gli angeli in colonna!")
        } else if (Regex("(che ore sono|che ora è)", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, oreInLettere(LocalDateTime.now()))
        } else if (botRegex.sloganMatch(message) != null) {
            rispondiAsText(this, message, slogan(botRegex.sloganMatch(message)))
        } else if (Regex("^!source$").matches(message.text)) {
            rispondiAsText(this, message, "http://github.com/ggiambo/SgruntBot")
        } else if (Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE).matches(message.text)) {
            val fortune = Runtime.getRuntime().exec("fortune -sa it")
                .inputStream
                .reader()
                .readText()
            rispondi(this, message, fortune)
        } else if (Regex("^@?sgrunt(y|bot) .*smetti.*", RegexOption.IGNORE_CASE).matches(message.text)) {
            if (message.from.id == 252800958L) {
                rispondi(this, message, "Col cazzo!")
            } else {
                pausedTime = LocalDateTime.now()
                rispondi(this, message, "Ok, sto zitto 5 minuti. :(")
            }
        } else if (Regex("^sgrunt(bot|y|olino|olomeo)", RegexOption.IGNORE_CASE).matches(message.text)) {
            if (message.from.id == 32657811L) {
                rispondi(this, message, "Ciao papà!")
            } else {
                val reply = listOf(
                    "Cazzo vuoi!?!",
                    "Chi mi chiama?",
                    "E io che c'entro adesso?",
                    "Farò finta di non aver sentito",
                    "Sgru' che... smuà!"
                )
                rispondi(this, message, reply[Random.nextInt(reply.size - 1)])
            }
        } else if (Regex("coccol(o|ino)", RegexOption.IGNORE_CASE).matches(message.text)
            && message.from.id == 32657811L
        ) {
            rispondi(this, message, "Non chiamarmi così davanti a tutti!")
        } else if (Regex("(^!btc$|quanto vale un bitcoin)", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, bitcoinvalue("USD"))
        } else if (Regex("^!btce$", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, bitcoinvalue("EUR"))
        } else if (Regex("^!id$", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, "Il tuo id: ${message.from.id}")
        } else if (Regex("(negr|negher)", RegexOption.IGNORE_CASE).matches(message.text)
            && !Regex("negrini", RegexOption.IGNORE_CASE).matches(message.text)
        ) {
            rispondi(this, message, "Lamin mi manchi.")
        } else if (Regex("bellissim", RegexOption.IGNORE_CASE).matches(message.text) && pignolo) {
            if (Random.nextBoolean()) {
                rispondi(this, message, "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!")
            } else {
                rispondi(this, message, "IO sono bellissimo! ....vabbé, facciamo a turni.")
            }
        } else if (Regex("rogan", RegexOption.IGNORE_CASE).matches(message.text)) {
            rispondi(this, message, "Cheppalle! Yawn!")
        } else if (botRegex.parlaMatch(message) != null) {
            executeAsync(SendMessage("-1001103213994", "Mi dicono di dire: ${botRegex.parlaMatch(message)}"))
        } else if (botRegex.aesMatch(message) != null) {
            if (botRegex.aesMatch(message) == "d") {
                rispondi(this, message, aesDecrypt(botRegex.aesMatch(message, 3), botRegex.aesMatch(message, 2)))
            } else {
                rispondi(this, message, aesEncrypt(botRegex.aesMatch(message, 3), botRegex.aesMatch(message, 2)))
            }
        } else if (Regex("^!last\$", RegexOption.IGNORE_CASE).matches(message.text) && lastAuthor != null) {
            val message = SendMessage()
            message.chatId = "-1001103213994"
            message.parseMode = ParseMode.MARKDOWN
            message.text = slogan(lastAuthor)
            executeAsync(message)
        } else if (botRegex.canzoMatch(message) != null) {
            canzo(this, message, botRegex.canzoMatch(message))
        } else if (botRegex.bullshitMatch(message) != null) {
            val value = botRegex.bullshitMatch(message)
            val eur = bullshitInEuro(value)
            if (eur != 0.0) {
                rispondi(this, message, "${value} bullshit corrispondono a #{eur} pregiati euro.")
            } else {
                rispondi(this, message, "Non ci riesco.")
            }
        }
    }

    private fun storeLastAuthor(message: Message) {
        if (message.from.userName != null) {
            lastAuthor = message.from.userName
        } else {
            lastAuthor = message.from.firstName
        }
    }

}