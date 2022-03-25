package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import java.awt.Color
import java.awt.Font
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class Tappeto(private val sgruntBot: SgruntBot) : Action, HasHalp {

    private val regex = Regex("^!tappeto (.*)\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val cosa = regex.find(message.text)?.groupValues?.get(1)
        if (cosa != null) {
            val chi = if (message.from.userName != null) message.from.userName else message.from.firstName
            val tappeto = alTappeto(chi, cosa)
            val sendPhoto = SendPhoto()
            sendPhoto.chatId = message.chat.id.toString()
            sendPhoto.replyToMessageId = message.messageId
            sendPhoto.parseMode = ParseMode.HTML
            sendPhoto.photo = tappeto
            sendPhoto.caption = "$chi manda $cosa al tappeto!"
            sgruntBot.rispondi(sendPhoto)
        }
    }

    override fun halp() = "<b>!tappeto</b> <i>chi o cosa</i> chi o cosa vuoi mandare al tappeto oggi?"

    fun alTappeto(chi: String, cosa: String): InputFile {

        val image = ImageIO.read(this::class.java.getResourceAsStream("/tappeto.jpg"))

        val graphics = image.graphics
        graphics.font = Font("Arial", Font.BOLD, 22)

        graphics.color = Color.LIGHT_GRAY
        graphics.drawString(chi, 200, 130)

        graphics.color = Color.RED
        graphics.drawString(cosa, 300, 530)

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)

        return InputFile(ByteArrayInputStream(os.toByteArray()), "tappeto.jpg")
    }

}
