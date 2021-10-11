import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.utils.CanzoUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message

class CanzoUtilsTest {

    private val canzoUtils = CanzoUtils()

    @Test
    fun canzoTtest() {
        val bot = Mockito.mock(Bot::class.java)
        val captor = ArgumentCaptor.forClass(SendAudio::class.java)

        val message = Message().apply {
            this.messageId = 42
            chat = Chat().apply {
                this.id = 99
            }
        }

        canzoUtils.canzo(bot, message, "2 Second Video")

        Mockito.verify(bot, Mockito.times(1)).executeAsync(captor?.capture())
        val risposta = captor.allValues[0]

        assertThat(risposta.chatId).isEqualTo("99")
        assertThat(risposta.replyToMessageId).isEqualTo(42)
        assertThat(risposta.audio.mediaName).isEqualTo("2_Second_Video.mp3")
        assertThat(risposta.audio.newMediaFile.path).isEqualTo("/tmp/songs/2_Second_Video.mp3")
    }
}