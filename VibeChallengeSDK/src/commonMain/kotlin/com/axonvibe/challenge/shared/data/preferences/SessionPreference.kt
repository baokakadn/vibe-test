package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.domain.model.SessionStatus

expect class SessionPreference() {

    fun saveSession(session: SessionStatus)

    fun isSessionAvailable(): Boolean

    fun getSessionId(): String

    fun removeSessionPref()

}