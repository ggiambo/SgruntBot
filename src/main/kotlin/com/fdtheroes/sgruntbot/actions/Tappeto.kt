package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.awt.Color
import java.awt.Font
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class Tappeto : Action, HasHalp {

    private val regex = Regex("^!tappeto (.*)\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        val cosa = regex.find(ctx.message.text)?.groupValues?.get(1)
        if (cosa != null) {
            val chi = if (ctx.message.from.userName != null) ctx.message.from.userName else ctx.message.from.firstName
            val tappeto = alTappeto(chi, cosa)
            ctx.addResponse(ActionResponse.photo(tappeto))
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
