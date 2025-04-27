package com.fdtheroes.sgruntbot.models

import jakarta.persistence.*

@Entity(name = "nvps")
data class NameValuePair(
    @Id @Column(name = "name") @Enumerated(EnumType.STRING) var name: NameValuePairName,
    @Column(name = "value") var value: String,
) {
    enum class NameValuePairName() {
        GIT_HASH,
    }
}
