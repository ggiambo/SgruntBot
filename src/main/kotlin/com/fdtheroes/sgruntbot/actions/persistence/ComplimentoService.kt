package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.stereotype.Service

@Service
class ComplimentoService(private val complimentoRepository: ComplimentoRepository) {

    fun saveOrUpdate(userId: Long, complimento: String) {
        if (complimentoRepository.existsById(userId)) {
            complimentoRepository.save(Complimento(userId, complimento))
        } else {
            complimentoRepository.createComplimento(userId, complimento)
        }
    }

    fun get(userId: Long): String? {
        return complimentoRepository.findById(userId)
            .map { it.complimento }
            .orElse(null)
    }
}