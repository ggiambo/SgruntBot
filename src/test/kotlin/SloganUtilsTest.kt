import com.fdtheroes.sgruntbot.utils.SloganUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SloganUtilsTest {

    val sloganUtils = SloganUtils()

    @Test
    fun testSlogan() {
        assertThat(sloganUtils.slogan("contenuto")).contains("contenuto")
    }
}