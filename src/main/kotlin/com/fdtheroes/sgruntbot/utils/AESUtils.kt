package com.fdtheroes.sgruntbot.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Classe sponsorizzata dalla Suora Malefica.
 */
class AESUtils {

    fun aesEncrypt(text: String?, key: String?): String {
        if (text == null || key == null) {
            return "non ci riesco :("
        }
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key))
        val enctext = cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(enctext)
    }

    fun aesDecrypt(enctext: String?, key: String?): String {
        if (enctext == null || key == null) {
            return "non ci riesco :("
        }
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key))
        val text = cipher.doFinal(Base64.getDecoder().decode(enctext))
        return text.decodeToString()
    }

    fun getSecretKey(key: String): SecretKeySpec {
        val secretKey = MessageDigest.getInstance("MD5")
            .digest(key.toByteArray(StandardCharsets.UTF_8))
            .copyOf(16)
        return SecretKeySpec(secretKey, "AES")
    }

}