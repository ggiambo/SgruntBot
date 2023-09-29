package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.ErrePiGi
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ErrePiGiRepository : JpaRepository<ErrePiGi, Long>