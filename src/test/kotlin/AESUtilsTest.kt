import com.fdtheroes.sgruntbot.utils.AESUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class AESUtilsTest {

    private val aesUtils = AESUtils()

    @Test
    fun testAesEncrypt() {
        val enctext = aesUtils.aesEncrypt("La Suora è un mostro di bravura", "balle(tm)")
        Assertions.assertThat(enctext).isEqualTo("qiSN2BDEOdsuNeXDU5k3pEkuyNRFPztFZBYJLQt6EOREoH61KaTBd+Na78a0G4Lu")
    }

    @Test
    fun testAesDencrypt() {
        val text = aesUtils.aesDecrypt("qiSN2BDEOdsuNeXDU5k3pEkuyNRFPztFZBYJLQt6EOREoH61KaTBd+Na78a0G4Lu", "balle(tm)")
        Assertions.assertThat(text).isEqualTo("La Suora è un mostro di bravura")
    }
}