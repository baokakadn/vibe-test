package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.domain.model.User

expect class UserPreference() {

    fun saveUser(user: User)

    fun isTimeJoinedAvailable(): Boolean

    fun getTimeJoined(): String

    fun removeSessionPref()

}