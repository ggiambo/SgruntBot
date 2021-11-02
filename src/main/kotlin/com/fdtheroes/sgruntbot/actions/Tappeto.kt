package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO


class Tappeto : Action {

    private val regex = Regex("^!tappeto (.*)\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val cosa = regex.find(message.text)?.groupValues?.get(1)
        if (cosa != null) {
            val chi = if (message.from.userName != null) message.from.userName else message.from.firstName
            val tappeto = alTappeto(chi, cosa)
            val sendPhoto = SendPhoto()
            sendPhoto.chatId = BotUtils.chatId
            sendPhoto.replyToMessageId = message.messageId
            sendPhoto.photo = tappeto
            BotUtils.rispondi(sendPhoto)
        }
    }


    fun alTappeto(chi: String, cosa: String): InputFile {
        val image: BufferedImage = ImageIO.read(this.javaClass.getResourceAsStream("tappeto.png"))
        val font = Font("Arial", Font.BOLD, 18)

        val g = image.graphics
        g.font = font
        g.color = Color.GREEN
        g.drawString(chi, 0, 20)
        g.drawString(cosa, 10, 0)

        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        val fis = ByteArrayInputStream(os.toByteArray())

        return InputFile(fis, "$chi mette $cosa al tappeto!")
    }

}