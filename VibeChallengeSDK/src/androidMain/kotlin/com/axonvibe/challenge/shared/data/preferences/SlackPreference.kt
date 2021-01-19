package com.axonvibe.challenge.shared.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.AVATAR_KEY
import com.axonvibe.challenge.shared.configutation.NAME_KEY
import com.axonvibe.challenge.shared.configutation.TOKEN_KEY
import com.axonvibe.challenge.shared.configutation.USER_KEY
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.model.User
import com.github.florent37.preferences.application

actual class SlackPreference {

    private val sharedPref: SharedPreferences =
        application.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)

    actual fun storeProfile(profile: Info) {
        sharedPref.edit().putString(TOKEN_KEY, profile.user.accessToken).apply()
        sharedPref.edit().putString(NAME_KEY, profile.user.displayName).apply()
        sharedPref.edit().putString(AVATAR_KEY, profile.user.image).apply()
    }

    actual fun getProfile(): Response<Info> {
        var profile = Info()
        val user = User()
        if (sharedPref.getString(TOKEN_KEY, "") != null) {
            user.accessToken = sharedPref.getString(TOKEN_KEY, "")!!
        }

        if (sharedPref.getString(NAME_KEY, "") != null) {
            user.displayName = sharedPref.getString(NAME_KEY, "")!!
        }

        if (sharedPref.getString(AVATAR_KEY, "") != null) {
            user.image = sharedPref.getString(AVATAR_KEY, "")!!
        }
        profile.user = user
        return Response.Success(profile)
    }

    actual fun hasKey(key: String): Boolean {
        return sharedPref.contains(key)
    }

    actual fun deleteAll() {
        sharedPref.edit().clear().commit()
    }
}