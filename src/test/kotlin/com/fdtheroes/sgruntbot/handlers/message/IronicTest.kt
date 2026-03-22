package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IronicTest : BaseTest() {

    @Test
    fun ironic() {
        val ironic = Ironic(botUtils, botConfig)
        assertEquals("\"sOnO Un mAsChIo vIrIlE\"", ironic.ironic("Sono un maschio virile"))
    }
}