import com.fdtheroes.sgruntbot.utils.OreUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class OreUtilsTest {

    private val oreUtils = OreUtils()

    @Test
    fun testMezzogiorno() {
        assertThat(oreUtils.oreInLettere(time(12, 25))).isEqualTo("mezzogiorno e venticinque (precisamente 12:25) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 26))).isEqualTo("mezzogiorno e venticinque (precisamente 12:26) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 27))).isEqualTo("mezzogiorno e venticinque (precisamente 12:27) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 28))).isEqualTo("mezzogiorno e venticinque (precisamente 12:28) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 29))).isEqualTo("mezzogiorno e venticinque (precisamente 12:29) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 30))).isEqualTo("mezzogiorno e mezzo (precisamente 12:30) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 31))).isEqualTo("mezzogiorno e mezzo (precisamente 12:31) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 32))).isEqualTo("mezzogiorno e mezzo (precisamente 12:32) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 33))).isEqualTo("mezzogiorno e mezzo (precisamente 12:33) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 34))).isEqualTo("mezzogiorno e mezzo (precisamente 12:34) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 35))).isEqualTo("mezzogiorno e trentacinque circa (precisamente 12:35) ok?)")
        assertThat(oreUtils.oreInLettere(time(12, 36))).isEqualTo("mezzogiorno e trentacinque circa (precisamente 12:36) ok?)")
    }

    @Test
    fun testPomeriggio() {
        assertThat(oreUtils.oreInLettere(time(16, 0))).isEqualTo("le quattro in punto (precisamente 16:00) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 1))).isEqualTo("le quattro in punto (precisamente 16:01) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 2))).isEqualTo("le quattro in punto (precisamente 16:02) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 3))).isEqualTo("le quattro in punto (precisamente 16:03) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 4))).isEqualTo("le quattro in punto (precisamente 16:04) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 5))).isEqualTo("le quattro e cinque (precisamente 16:05) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 6))).isEqualTo("le quattro e cinque (precisamente 16:06) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 7))).isEqualTo("le quattro e cinque (precisamente 16:07) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 8))).isEqualTo("le quattro e cinque (precisamente 16:08) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 9))).isEqualTo("le quattro e cinque (precisamente 16:09) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 10))).isEqualTo("le quattro e dieci (precisamente 16:10) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 11))).isEqualTo("le quattro e dieci (precisamente 16:11) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 12))).isEqualTo("le quattro e dieci (precisamente 16:12) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 13))).isEqualTo("le quattro e dieci (precisamente 16:13) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 14))).isEqualTo("le quattro e dieci (precisamente 16:14) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 15))).isEqualTo("le quattro e un quarto (precisamente 16:15) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 16))).isEqualTo("le quattro e un quarto (precisamente 16:16) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 17))).isEqualTo("le quattro e un quarto (precisamente 16:17) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 18))).isEqualTo("le quattro e un quarto (precisamente 16:18) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 19))).isEqualTo("le quattro e un quarto (precisamente 16:19) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 20))).isEqualTo("le quattro e venti (precisamente 16:20) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 21))).isEqualTo("le quattro e venti (precisamente 16:21) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 22))).isEqualTo("le quattro e venti (precisamente 16:22) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 23))).isEqualTo("le quattro e venti (precisamente 16:23) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 24))).isEqualTo("le quattro e venti (precisamente 16:24) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 25))).isEqualTo("le quattro e venticinque (precisamente 16:25) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 26))).isEqualTo("le quattro e venticinque (precisamente 16:26) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 27))).isEqualTo("le quattro e venticinque (precisamente 16:27) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 28))).isEqualTo("le quattro e venticinque (precisamente 16:28) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 29))).isEqualTo("le quattro e venticinque (precisamente 16:29) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 30))).isEqualTo("le quattro e mezzo (precisamente 16:30) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 31))).isEqualTo("le quattro e mezzo (precisamente 16:31) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 32))).isEqualTo("le quattro e mezzo (precisamente 16:32) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 33))).isEqualTo("le quattro e mezzo (precisamente 16:33) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 34))).isEqualTo("le quattro e mezzo (precisamente 16:34) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 35))).isEqualTo("le quattro e trentacinque circa (precisamente 16:35) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 36))).isEqualTo("le quattro e trentacinque circa (precisamente 16:36) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 37))).isEqualTo("le quattro e trentacinque circa (precisamente 16:37) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 38))).isEqualTo("le quattro e trentacinque circa (precisamente 16:38) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 39))).isEqualTo("le quattro e trentacinque circa (precisamente 16:39) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 40))).isEqualTo("le cinque meno venti (precisamente 16:40) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 41))).isEqualTo("le cinque meno venti (precisamente 16:41) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 42))).isEqualTo("le cinque meno venti (precisamente 16:42) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 43))).isEqualTo("le cinque meno venti (precisamente 16:43) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 44))).isEqualTo("le cinque meno venti (precisamente 16:44) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 45))).isEqualTo("le cinque meno un quarto (precisamente 16:45) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 46))).isEqualTo("le cinque meno un quarto (precisamente 16:46) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 47))).isEqualTo("le cinque meno un quarto (precisamente 16:47) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 48))).isEqualTo("le cinque meno un quarto (precisamente 16:48) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 49))).isEqualTo("le cinque meno un quarto (precisamente 16:49) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 50))).isEqualTo("le cinque meno dieci (precisamente 16:50) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 51))).isEqualTo("le cinque meno dieci (precisamente 16:51) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 52))).isEqualTo("le cinque meno dieci (precisamente 16:52) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 53))).isEqualTo("le cinque meno dieci (precisamente 16:53) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 54))).isEqualTo("le cinque meno dieci (precisamente 16:54) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 55))).isEqualTo("le cinque meno cinque (precisamente 16:55) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 56))).isEqualTo("le cinque meno cinque (precisamente 16:56) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 57))).isEqualTo("le cinque meno cinque (precisamente 16:57) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 58))).isEqualTo("le cinque meno cinque (precisamente 16:58) ok?)")
        assertThat(oreUtils.oreInLettere(time(16, 59))).isEqualTo("le cinque meno cinque (precisamente 16:59) ok?)")
    }

    private fun time(hour: Int, minutes: Int): LocalDateTime {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minutes))
    }

}