package com.axonvibe.challenge.shared.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    @SerialName("slack_id") val slack_id: String = "",
    @SerialName("image") var image: String = "",
    @SerialName("score") var score: Int = 0
)