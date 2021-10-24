package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Aes : Action {

    private val regex = Regex("^!aes(d?) ([^ ]+) (.*)$")

    override fun doAction(message: Message, context: Context) {
        val groupValues = regex.find(message.text)?.groupValues
        if (groupValues?.size == 4) {
            val decrypt = groupValues[1]
            val key = groupValues[2]
            val body = groupValues[3]
            if (decrypt.isEmpty()) {
                BotUtils.instance.rispondi(message, encrypt(key, body))
            } else {
                BotUtils.instance.rispondi(message, decrypt(key, body))
            }
        }
    }

    private fun encrypt(key: String, text: String): String {
        if (text.isEmpty() || key.isEmpty()) {
            return "non ci riesco :("
        }
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key))
        val enctext = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(enctext)
    }

    private fun decrypt(key: String, enctext: String): String {
        if (enctext.isEmpty() || key.isEmpty()) {
            return "non ci riesco :("
        }
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