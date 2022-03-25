package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KarmaRepository : CrudRepository<Karma, Long>