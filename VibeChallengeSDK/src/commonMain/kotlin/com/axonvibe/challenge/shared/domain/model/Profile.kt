package com.axonvibe.challenge.shared.domain.model

import co.touchlab.firebase.firestore.Timestamp
import co.touchlab.firebase.firestore.timestampNow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Transient

@Serializable
data class Profile(
    var ok: Boolean = true,
    @SerialName("access_token")
    var accessToken: String = "",
    @SerialName("scope")
    var scope: String = "",
    @SerialName("user_id")
    var userId: String = "",
    @SerialName("team_id")
    var teamId: String = "",
    @SerialName("enterprise_id")
    var enterpriseId: String? = null,
    @SerialName("user")
    var person: Person = Person(),
    @SerialName("team")
    var team: Team = Team()
)

@Serializable
data class Person(
    @SerialName("name")
    var name: String = "",
    @SerialName("id")
    var id: String = ""
)

@Serializable
data class Info(
    var ok: Boolean = true,
    @SerialName("profile")
    var user: User = User()
)

@Serializable
data class User(
    var accessToken: String = "",
    @SerialName("real_name")
    var realName: String = "",
    @SerialName("display_name")
    var displayName: String = "",
    @SerialName("image_original")
    var image: String = "",
    @SerialName("email")
    var email: String = "",
    @Transient
    var timeJoined: Timestamp? = null,
    @SerialName("status")
    var status: String = "",
    @Transient
    var idSession: String = "",
    @Transient
    var score: Int = 0
)

@Serializable
data class Team(
    var id: String = ""
)
