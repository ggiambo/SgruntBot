import com.fdtheroes.sgruntbot.utils.MoneyUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MoneyUtilsTest {

    val moneyUtils = MoneyUtils()

    @Test
    fun testBitcoinvalue() {
       assertThat(moneyUtils.bitcoinvalue("CHF")).startsWith("Il buttcoin vale ").endsWith(" CHF")
       assertThat(moneyUtils.bitcoinvalue("USD")).startsWith("Il buttcoin vale ").endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testBullshitInEuro() {
        assertThat(moneyUtils.bullshitInEuro(null)).isEqualTo(0.0)
        assertThat(moneyUtils.bullshitInEuro("42")).isGreaterThan(0.0)
    }
}