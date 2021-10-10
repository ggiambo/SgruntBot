import com.fdtheroes.sgruntbot.utils.WikiUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class WikiUtilsUtils {

    private val wikiUtils = WikiUtils()

    @Test
    fun testWikiSearch() {
        var risp = ""
        wikiUtils.wikiSearch("giambo") { testo: String -> risp = testo }
        Assertions.assertThat(risp)
            .startsWith("Il giambo (in greco antico: ")
            .endsWith("https://it.wikipedia.org/wiki/Giambo")
    }

}