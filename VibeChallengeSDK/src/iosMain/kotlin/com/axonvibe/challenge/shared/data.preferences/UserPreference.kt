package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.util.TIME_JOINED
import com.axonvibe.challenge.shared.util.TIME_JOINED_NOT_FOUND
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual class UserPreference {

    actual fun saveUser(user: User) {
        val timeJoined = user.timeJoined
        if (timeJoined != null) NSUserDefaults.standardUserDefaults.setValue(
            timeJoined.toString(),
            forKey = TIME_JOINED
        )
    }

    actual fun isTimeJoinedAvailable(): Boolean {
        return NSUserDefaults.standardUserDefaults.objectForKey(TIME_JOINED) != null
    }

    actual fun getTimeJoined(): String {
        val timeJoined = NSUserDefaults.standardUserDefaults.stringForKey(TIME_JOINED)
        if (timeJoined != null) {
            return timeJoined
        }
        return TIME_JOINED_NOT_FOUND
    }

    actual fun removeSessionPref() {
        val appDomain = NSBundle.mainBundle().bundleIdentifier
        if (appDomain != null) {
            NSUserDefaults.standardUserDefaults().removePersistentDomainForName(appDomain)
        }
    }

}