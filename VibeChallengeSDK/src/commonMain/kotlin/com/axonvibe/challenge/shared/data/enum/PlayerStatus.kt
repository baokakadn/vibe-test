package com.axonvibe.challenge.shared.data.enum

enum class PlayerStatus(val status: String) {
    PLAYER_ALREADY_IN_SESSION("Already in session!"),
    PLAYER_HAS_BEEN_KICKED("You have been kicked!"),
    ANOTHER_DEVICE_HAVE_JOINED("Another device have been joined"),
    INACTIVE_USER("User is inactive!"),
    HAVE_NOT_JOINED("Have not joined"),
    ACTIVE_USER("active"),
    JOINED_INVALID_SESSION("Joined invalid session")
}