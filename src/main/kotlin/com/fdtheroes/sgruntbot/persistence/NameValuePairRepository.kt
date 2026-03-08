package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.NameValuePair
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
interface NameValuePairRepository : CrudRepository<NameValuePair, NameValuePair.NameValuePairName> {
    fun getValue(name: NameValuePair.NameValuePairName) = findByIdOrNull(name)?.value
}