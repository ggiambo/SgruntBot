import com.fdtheroes.sgruntbot.utils.UserUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

class UserUtilsTest {

    private val userUtils = UserUtils()

    @Test
    fun testGetUserLink_username() {
        val message = Message()
        val user = User().apply {
            id = 99
            userName = "taddeo"
        }
        message.from = user

        Assertions.assertThat(userUtils.getUserLink(message)).isEqualTo("[taddeo](tg://user?id=99)")
    }

    @Test
    fun testGetUserLink_firstName() {
        val message = Message()
        val user = User().apply {
            id = 99
            firstName = "babbeo"
        }
        message.from = user

        Assertions.assertThat(userUtils.getUserLink(message)).isEqualTo("[babbeo](tg://user?id=99)")
    }
}