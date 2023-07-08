package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class Aes : Action, HasHalp {

    private val regex = Regex("^!aes(d?) ([^ ]+) (.*)$")

    override fun doAction(ctx: ActionContext) {
        val groupValues = regex.find(ctx.message.text)?.groupValues
        if (groupValues?.size == 4) {
            val decrypt = groupValues[1]
            val key = groupValues[2]
            val body = groupValues[3]
            if (decrypt.isEmpty()) {
                ctx.addResponse(ActionResponse.message(encrypt(key, body)))
            } else {
                ctx.addResponse(ActionResponse.message(decrypt(key, body)))
            }
        }
    }

    override fun halp() =
        "<b>!aes</b> <i>chiave</i> <i>testo</i> per codificare\n!<b>aesd</b> <i>chiave</i> <i>testo</i> per decodificare"

    private fun encrypt(key: String, text: String): String {
        if (text.isEmpty() || key.isEmpty()) {
            return "non ci riesco :("
        }
        @SuppressWarnings("kotlin:S5542")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key))
        val enctext = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(enctext)
    }

    private fun decrypt(key: String, enctext: String): String {
        if (enctext.isEmpty() || key.isEmpty()) {
            return "non ci riesco :("
        }
        @SuppressWarnings("kotlin:S5542")
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key))
        val text = cipher.doFinal(Base64.getDecoder().decode(enctext))
        return text.decodeToString()
    }

    private fun getSecretKey(key: String): SecretKeySpec {
        val secretKey = MessageDigest.getInstance("MD5")
            .digest(key.toByteArray(StandardCharsets.UTF_8))
            .copyOf(16)
        return SecretKeySpec(secretKey, "AES")
    }

}
