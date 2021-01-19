package com.axonvibe.challenge.shared.data.firebaseCloud

import co.touchlab.firebase.firestore.*
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.enum.SessionAction
import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.util.*

class FirebaseCloud {
    fun createUser(idSession: String, user: User): Response<String> {
        try {
            val userMap = prepareUserMap(user)
            isPlayerExist(idSession, user)
                .setData(
                    userMap
                )
            return Response.Success(idSession)
        } catch (ex: Exception) {
            return Response.Error(ex)
        }
    }

    fun updateUser(idSession: String, user: User): Response<String> {
        try {
            val userMap = prepareUserMap(user)
            userMap.remove(SCORE)
            isPlayerExist(idSession, user)
                .updateData(
                    userMap
                )
            return Response.Success(idSession)
        } catch (ex: Exception) {
            return Response.Error(ex)
        }
    }

    fun userAnswerQuestion(user: User, questionId: String, questionIdx: Int): Response<String> {
        try {
            val questionRef = questionResultRef(questionId, user)
            questionRef.setData(prepareAnswer(questionIdx))
            return Response.Success(SessionAction.CALCULATING.value)
        } catch (ex: Exception) {
            return Response.Error(ex)
        }
    }

    suspend fun getPlayerInSession(idSession: String, user: User): Map<String, Any?>? {
        return isPlayerExist(idSession, user).suspendGet().data_()
    }

    fun isPlayerExist(idSession: String, user: User): DocumentReference {
        return getFirebaseInstance().collection(PLAYER_IN_SESSION_COLLECTION)
            .document(idSession).collection(PLAYER_COLLECTION)
            .document(user.displayName)
    }

    suspend fun getSessionIfValid(idSession: String): Map<String, Any?>? {
        return getFirebaseInstance().collection(SESSION_COLLECTION)
            .document(idSession).suspendGet().data_()
    }

    fun sessionStateRef(sessionId: String): DocumentReference {
        return getFirebaseInstance().collection(SESSION_STATE_COLLECTION).document(sessionId)
    }

    fun resultRef(sessionId: String): DocumentReference {
        return getFirebaseInstance().collection(RESULTS).document(sessionId)
    }

    fun questionResultRef(questionId: String, user: User): DocumentReference {
        return resultRef(user.idSession).collection(questionId).document(user.displayName)
    }
}
