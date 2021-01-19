package com.axonvibe.challenge.shared.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.util.TIME_JOINED
import com.axonvibe.challenge.shared.util.TIME_JOINED_NOT_FOUND
import com.axonvibe.challenge.shared.util.USER_PREF
import com.github.florent37.preferences.application

actual class UserPreference {

    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)

    actual fun saveUser(user: User) {
        val timeJoined = user.timeJoined
        if (timeJoined != null) sharedPref.edit().putString(TIME_JOINED, timeJoined.toString())
            .apply()
    }

    actual fun isTimeJoinedAvailable(): Boolean {
        return sharedPref.contains(TIME_JOINED)
    }

    actual fun getTimeJoined(): String {
        val timeJoined = sharedPref.getString(TIME_JOINED, "")
        if (timeJoined.isNotBlank()) {
            return timeJoined
        }
        return TIME_JOINED_NOT_FOUND
    }

    actual fun removeSessionPref() {
        sharedPref.edit().clear().commit()
    }

}