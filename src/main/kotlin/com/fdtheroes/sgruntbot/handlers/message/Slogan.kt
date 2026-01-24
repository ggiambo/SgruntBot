package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper
import java.net.URLDecoder

@Service
class Slogan(botUtils: BotUtils, botConfig: BotConfig, private val jsonMapper: JsonMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!slogan (.*)$", RegexOption.IGNORE_CASE)
    private val urlSlogan = "https://www.oberlo.com/tools/slogan-generator.data"

    override fun handle(message: Message) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null) {
            val slogan = URLDecoder.decode(fetchSlogan(testo), Charsets.UTF_8)
            botUtils.rispondi(ActionResponse.message(slogan), message)
        }
    }

    override fun halp() = "<b>!slogan</b> <i>testo</i> uno slogan per il testo!"

    fun fetchSlogan(testo: String): String {
        val client = OkHttpClient().newBuilder().build()
        val requestBody = FormBody.Builder().add("search-query", testo).build()
        val request = Request.Builder()
            .post(requestBody)
            .url(urlSlogan)
            .build()
        val responseBody = client.newCall(request).execute().body
        val slogans = jsonMapper.readTree(responseBody.byteStream()).toList().drop(6)
        return slogans.random().stringValue()
    }

    fun fetchSlogan(utente: User): String {
        val res = fetchSlogan(utente.firstName)
        return res.replace(utente.firstName, botUtils.getUserLink(utente))
    }

}
