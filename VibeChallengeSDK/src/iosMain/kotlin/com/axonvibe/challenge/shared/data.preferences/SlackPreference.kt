package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.AVATAR_KEY
import com.axonvibe.challenge.shared.configutation.NAME_KEY
import com.axonvibe.challenge.shared.configutation.TOKEN_KEY
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.model.Profile
import com.axonvibe.challenge.shared.domain.model.User
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual class SlackPreference {

    actual fun storeProfile(profile: Info) {
        NSUserDefaults.standardUserDefaults.setValue(
            profile.user.accessToken,
            forKey = TOKEN_KEY
        )
        NSUserDefaults.standardUserDefaults.setValue(
            profile.user.displayName,
            forKey = NAME_KEY
        )
        NSUserDefaults.standardUserDefaults.setValue(
            profile.user.image,
            forKey = AVATAR_KEY
        )
    }

    actual fun getProfile(): Response<Info> {
        val token = NSUserDefaults.standardUserDefaults.stringForKey(TOKEN_KEY)
        val name = NSUserDefaults.standardUserDefaults.stringForKey(NAME_KEY)
        val avatar = NSUserDefaults.standardUserDefaults.stringForKey(AVATAR_KEY)

        return Response.Success(
            Info(
                user = User(accessToken = token!!, displayName = name!!, image = avatar!!)
            )
        )
    }

    actual fun hasKey(key: String): Boolean {
        return NSUserDefaults.standardUserDefaults.objectForKey(key) != null
    }

    actual fun deleteAll() {
        val appDomain = NSBundle.mainBundle().bundleIdentifier
        if (appDomain != null) {
            NSUserDefaults.standardUserDefaults().removePersistentDomainForName(appDomain)
        }
    }
}