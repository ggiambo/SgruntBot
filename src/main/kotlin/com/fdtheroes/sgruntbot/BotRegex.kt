package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.meta.api.objects.Message

class BotRegex {
    private val parolacce =
        Regex("((c|k)a(t|z)z(i|o)|(k|c)ulo|((^| )fica( |$))|vaffanculo|stronz(a|o|i|e)|coglion(a|e|i)|merda)")
    private val sloganmatch = Regex("^!slogan (.*)$", RegexOption.IGNORE_CASE)
    private val parlamatch = Regex("^!parla (.*)$", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val parlasuper = Regex("^!parlasuper (.*)$", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val chiera = Regex("^!chiera$", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val aesmatch = Regex("^!aes(d?) ([^ ]+) (.*)$")
    private val canzomatch = Regex("!canzone (.*)$", RegexOption.IGNORE_CASE)
    private val bullshitmatch = Regex("([0-9]+([,.][0-9]+)?)[ ]?(bs|bullshit)", RegexOption.IGNORE_CASE)
    private val wikimatch = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)
    private val googlematch = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)
    private val last = Regex("^!last$", RegexOption.IGNORE_CASE)

    fun parolacceMatch(message: Message) = parolacce.matches(message.text)
    fun sloganMatch(message: Message) = content(message, sloganmatch)
    fun parlaMatch(message: Message) = content(message, sloganmatch)
    fun parlaSuper(message: Message) = content(message, parlasuper)
    fun chiEra(message: Message) = chiera.matches(message.text)
    fun aesMatch(message: Message, group: Int = 1) = content(message, aesmatch, group)
    fun canzoMatch(message: Message) = content(message, canzomatch)
    fun bullshitMatch(message: Message) = content(message, bullshitmatch)
    fun wikiMatch(message: Message) = content(message, wikimatch)
    fun googleMatch(message: Message) = content(message, googlematch)
    fun last(message: Message) = chiera.matches(message.text)

    private fun content(message: Message, regex: Regex, group: Int = 1): String? {
        return regex.findAll(message.text)
            .map { it.groupValues[group] }
            .firstOrNull()
    }
}