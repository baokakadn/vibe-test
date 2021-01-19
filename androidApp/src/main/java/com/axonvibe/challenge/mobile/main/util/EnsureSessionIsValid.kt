package com.axonvibe.challenge.mobile.main.util

import com.axonvibe.challenge.shared.data.enum.PlayerStatus

fun String.ensureSessionIsValid(): Boolean {
    return this.isNotBlank() &&
            this != PlayerStatus.PLAYER_ALREADY_IN_SESSION.status &&
            this != PlayerStatus.HAVE_NOT_JOINED.status
}