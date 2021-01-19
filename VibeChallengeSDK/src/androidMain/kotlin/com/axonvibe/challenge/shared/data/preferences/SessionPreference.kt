package com.axonvibe.challenge.shared.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.util.SESSION_ID
import com.axonvibe.challenge.shared.util.SESSION_NOT_FOUND
import com.axonvibe.challenge.shared.util.SESSION_PREF
import com.github.florent37.preferences.application

actual class SessionPreference {

    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(SESSION_PREF, Context.MODE_PRIVATE)

    actual fun saveSession(session: SessionStatus) {
        sharedPref.edit().putString(SESSION_ID, session.id).apply()
    }

    actual fun isSessionAvailable(): Boolean {
        return sharedPref.contains(SESSION_PREF)
    }

    actual fun getSessionId(): String {
        val idSession = sharedPref.getString(SESSION_ID, "")
        if (idSession.isNotBlank()) {
            return idSession
        }
        return SESSION_NOT_FOUND
    }

    actual fun removeSessionPref() {
        sharedPref.edit().clear().commit()
    }
}