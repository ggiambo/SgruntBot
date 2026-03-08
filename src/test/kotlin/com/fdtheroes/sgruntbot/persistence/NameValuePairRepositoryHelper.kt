package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.NameValuePair
import org.mockito.kotlin.any
import org.mockito.kotlin.callRealMethod
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import java.util.*

class NameValuePairRepositoryHelper {

    private val repository = mutableMapOf<NameValuePair.NameValuePairName, NameValuePair>()
    private val mockNameValuePairRepository = mock<NameValuePairRepository> {
        on { save(any<NameValuePair>()) } doAnswer {
            val entity = it.component1<NameValuePair>()
            repository[entity.name] = entity
            entity
        }
        on { findById(any<NameValuePair.NameValuePairName>()) } doAnswer {
            val value = it.component1<NameValuePair.NameValuePairName>()
            Optional.ofNullable(repository[value])
        }
        on { getValue(any()) }.callRealMethod()
    }

    fun nameValuePairRepository() = mockNameValuePairRepository
    fun reset() = repository.clear()
}