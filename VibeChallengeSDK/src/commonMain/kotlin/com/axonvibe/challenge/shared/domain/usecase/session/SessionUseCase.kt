package com.axonvibe.challenge.shared.domain.usecase.session

import co.touchlab.firebase.firestore.data_
import co.touchlab.firebase.firestore.suspendGet
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.util.parseToSessionStatus

class SessionUseCase {
    val firebaseCloud = FirebaseCloud()

    suspend fun getSessionStatus(sessionId: String): SessionStatus {
        val sessionMap = firebaseCloud.sessionStateRef(sessionId).suspendGet().data_()
        return (sessionMap as Map<String, Any>).parseToSessionStatus()
    }
}