package com.axonvibe.challenge.shared.data.preferences

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.domain.model.Info

expect class SlackPreference() {

    fun storeProfile(profile: Info)

    fun getProfile(): Response<Info>

    fun hasKey(key: String): Boolean

    fun deleteAll()

}
