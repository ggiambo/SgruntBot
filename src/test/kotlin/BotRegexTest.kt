import com.fdtheroes.sgruntbot.BotRegex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.objects.Message

class BotRegexTest {

    val botRegex = BotRegex()

    @ParameterizedTest
    @ValueSource(strings = ["cazzone", "culona", " fica ", "stronzi", "merdah!"])
    fun testParolacceMatch(string: String) {
        Assertions.assertTrue(botRegex.parolacceMatch(message(string)))
    }

    @Test
    fun testParlaMatch() {
        Assertions.assertEquals("blah banf", botRegex.parlaMatch(message("!parla blah banf")))
        Assertions.assertEquals("blah banf", botRegex.parlaMatch(message("!PARLA blah banf")))
        Assertions.assertEquals(
            "la vispa teresa\nsotto i sette nani",
            botRegex.parlaMatch(message("!parla la vispa teresa\nsotto i sette nani"))
        )
        Assertions.assertNull(botRegex.parlaMatch(message(" !parla blan banf")))
        Assertions.assertNull(botRegex.parlaMatch(message("!parlasuper blan banf")))
    }

    @Test
    fun testParlaSuper() {
        Assertions.assertEquals("blah banf", botRegex.parlaSuper(message("!parlasuper blah banf")))
        Assertions.assertEquals("blah banf", botRegex.parlaSuper(message("!PARLASUPER blah banf")))
        Assertions.assertEquals(
            "la vispa teresa\nsotto i sette nani",
            botRegex.parlaSuper(message("!parlasuper la vispa teresa\nsotto i sette nani"))
        )
        Assertions.assertNull(botRegex.parlaSuper(message(" !parlasuper blan banf")))
        Assertions.assertNull(botRegex.parlaSuper(message("!parla blan banf")))
    }

    @Test
    fun testChiEra() {
        Assertions.assertTrue(botRegex.chiEra(message("!chiera")))
        Assertions.assertTrue(botRegex.chiEra(message("!CHIERA")))
        Assertions.assertFalse(botRegex.chiEra(message("!chiera blah banf")))
    }

    @Test
    fun testAesMatch() {
        Assertions.assertEquals("", botRegex.aesMatch(message("!aes one two")))
        Assertions.assertEquals("d", botRegex.aesMatch(message("!aesd one two")))
        Assertions.assertEquals("d", botRegex.aesMatch(message("!aesd one two"), 1))
        Assertions.assertEquals("one", botRegex.aesMatch(message("!aesd one two"), 2))
        Assertions.assertEquals("two", botRegex.aesMatch(message("!aesd one two"), 3))
        Assertions.assertEquals(null, botRegex.aesMatch(message("!aesd one"), 3))
    }

    @Test
    fun testCanzoMatch() {
        Assertions.assertEquals("la ballata dei troll", botRegex.canzoMatch(message("!canzone la ballata dei troll")))
        Assertions.assertEquals("la ballata dei troll", botRegex.canzoMatch(message("!CANZONE la ballata dei troll")))
        Assertions.assertEquals(null, botRegex.canzoMatch(message("!canzone")))
        Assertions.assertEquals("", botRegex.canzoMatch(message("!canzone ")))
    }

    @Test
    fun testBullshitMatch() {
        Assertions.assertEquals("42", botRegex.bullshitMatch(message("42 bs")))
        Assertions.assertEquals("42", botRegex.bullshitMatch(message("42bs")))
        Assertions.assertEquals("42", botRegex.bullshitMatch(message("42BS")))
        Assertions.assertEquals("42", botRegex.bullshitMatch(message("42 bUlLsHiT")))
        Assertions.assertEquals(null, botRegex.bullshitMatch(message("42   bs")))
    }

    @Test
    fun testWikiMatch() {
        Assertions.assertEquals("denti d'oro", botRegex.wikiMatch(message("!wiki denti d'oro")))
        Assertions.assertEquals("denti d'oro", botRegex.wikiMatch(message("!WIKI denti d'oro")))
        Assertions.assertEquals(null, botRegex.wikiMatch(message("!wiki")))
        Assertions.assertEquals("", botRegex.wikiMatch(message("!wiki ")))
    }

    @Test
    fun testGoogleMatch() {
        Assertions.assertEquals("inutile bidet", botRegex.googleMatch(message("!google inutile bidet")))
        Assertions.assertEquals("inutile bidet", botRegex.googleMatch(message("!GOOGLE inutile bidet")))
        Assertions.assertEquals(null, botRegex.googleMatch(message("!google")))
        Assertions.assertEquals("", botRegex.googleMatch(message("!google ")))
    }

    @Test
    fun testLast() {

    }

    private fun message(text: String): Message {
        return Message().apply {
            setText(text)
        }
    }
}