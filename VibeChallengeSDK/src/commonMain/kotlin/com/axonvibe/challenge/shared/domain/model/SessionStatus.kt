package com.axonvibe.challenge.shared.domain.model

import co.touchlab.firebase.firestore.Timestamp
import co.touchlab.firebase.firestore.timestampNow

data class SessionStatus(
    var questionID: String = "",
    var timeStart: Timestamp = timestampNow(),
    var id: String = "",
    var status: String = ""
)