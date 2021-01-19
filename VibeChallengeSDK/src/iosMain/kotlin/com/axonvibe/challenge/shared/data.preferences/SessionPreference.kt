package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.util.SESSION_ID
import com.axonvibe.challenge.shared.util.SESSION_NOT_FOUND
import com.axonvibe.challenge.shared.util.SESSION_PREF
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual class SessionPreference {

    actual fun saveSession(session: SessionStatus) {
        NSUserDefaults.standardUserDefaults.setValue(
            session.id,
            forKey = SESSION_ID
        )
    }

    actual fun isSessionAvailable(): Boolean {
        return NSUserDefaults.standardUserDefaults.objectForKey(SESSION_PREF) != null
    }

    actual fun getSessionId(): String {
        val idSession = NSUserDefaults.standardUserDefaults.stringForKey(SESSION_ID)
        if (idSession != null) {
            return idSession
        }
        return SESSION_NOT_FOUND
    }

    actual fun removeSessionPref() {
        val appDomain = NSBundle.mainBundle().bundleIdentifier
        if (appDomain != null) {
            NSUserDefaults.standardUserDefaults().removePersistentDomainForName(appDomain)
        }
    }

}