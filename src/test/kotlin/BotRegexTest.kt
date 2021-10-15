import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.objects.Message

class BotRegexTest {

    val botRegex = BotRegex()

    @ParameterizedTest
    @ValueSource(strings = ["cazzone", "culona", " fica ", "stronzi", "merdah!"])
    fun testParolacceMatch(string: String) {
        assertThat(botRegex.parolacceMatch(message(string))).isTrue
    }

    @Test
    fun testParlaMatch() {
        assertThat(botRegex.parlaMatch(message("!parla blah banf"))).isEqualTo("blah banf")
        assertThat(botRegex.parlaMatch(message("!PARLA blah banf"))).isEqualTo("blah banf")
        assertThat(botRegex.parlaMatch(message("!parla la vispa teresa\nsotto i sette nani")))
            .isEqualTo("la vispa teresa\nsotto i sette nani")
        assertThat(botRegex.parlaMatch(message(" !parla blan banf"))).isNull()
        assertThat(botRegex.parlaMatch(message("!parlasuper blan banf"))).isNull()
    }

    @Test
    fun testParlaSuper() {
        assertThat(botRegex.parlaSuper(message("!parlasuper blah banf"))).isEqualTo("blah banf")
        assertThat(botRegex.parlaSuper(message("!PARLASUPER blah banf"))).isEqualTo("blah banf")
        assertThat(botRegex.parlaSuper(message("!parlasuper la vispa teresa\nsotto i sette nani")))
            .isEqualTo("la vispa teresa\nsotto i sette nani")
        assertThat(botRegex.parlaSuper(message(" !parlasuper blan banf"))).isNull()
        assertThat(botRegex.parlaSuper(message("!parla blan banf"))).isNull()
    }

    @Test
    fun testChiEra() {
        assertThat(botRegex.chiEra(message("!chiera"))).isTrue
        assertThat(botRegex.chiEra(message("!CHIERA"))).isTrue
        assertThat(botRegex.chiEra(message("!chiera blah banf"))).isFalse
    }

    @Test
    fun testAesMatch() {
        assertThat(botRegex.aesMatch(message("!aes one two"))).isEmpty()
        assertThat(botRegex.aesMatch(message("!aesd one two"))).isEqualTo("d")
        assertThat(botRegex.aesMatch(message("!aesd one two"), 1)).isEqualTo("d")
        assertThat(botRegex.aesMatch(message("!aesd one two"), 2)).isEqualTo("one")
        assertThat(botRegex.aesMatch(message("!aesd one two"), 3)).isEqualTo("two")
        assertThat(botRegex.aesMatch(message("!aesd one"), 3)).isNull()
    }

    @Test
    fun testCanzoMatch() {
        assertThat(botRegex.canzoMatch(message("!canzone la ballata dei troll"))).isEqualTo("la ballata dei troll")
        assertThat(botRegex.canzoMatch(message("!CANZONE la ballata dei troll"))).isEqualTo("la ballata dei troll")
        assertThat(botRegex.canzoMatch(message("!canzone"))).isNull()
        assertThat(botRegex.canzoMatch(message("!canzone "))).isEmpty()
    }

    @Test
    fun testBullshitMatch() {
        assertThat(botRegex.bullshitMatch(message("42 bs"))).isEqualTo("42")
        assertThat(botRegex.bullshitMatch(message("42bs"))).isEqualTo("42")
        assertThat(botRegex.bullshitMatch(message("42BS"))).isEqualTo("42")
        assertThat(botRegex.bullshitMatch(message("42 bUlLsHiT"))).isEqualTo("42")
        assertThat(botRegex.bullshitMatch(message("42   bs"))).isNull()
    }

    @Test
    fun testWikiMatch() {
        assertThat(botRegex.wikiMatch(message("!wiki denti d'oro"))).isEqualTo("denti d'oro")
        assertThat(botRegex.wikiMatch(message("!WIKI denti d'oro"))).isEqualTo("denti d'oro")
        assertThat(botRegex.wikiMatch(message("!wiki"))).isNull()
        assertThat(botRegex.wikiMatch(message("!wiki "))).isEmpty()
    }

    @Test
    fun testGoogleMatch() {
        assertThat(botRegex.googleMatch(message("!google inutile bidet"))).isEqualTo("inutile bidet")
        assertThat(botRegex.googleMatch(message("!GOOGLE inutile bidet"))).isEqualTo("inutile bidet")
        assertThat(botRegex.googleMatch(message("!google"))).isNull()
        assertThat(botRegex.googleMatch(message("!google "))).isEmpty()
    }

    private fun message(text: String): Message {
        return Message().apply {
            setText(text)
        }
    }
}