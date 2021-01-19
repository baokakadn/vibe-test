package com.axonvibe.challenge.shared.util

import co.touchlab.firebase.firestore.timestampNow
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.viewModel.playerInSession.PlayerInSessionViewModel
import com.axonvibe.challenge.shared.util.TIME_JOINED
import com.axonvibe.challenge.shared.util.STATE

// EX : {hah:{name=nhung, score=123}} => {"hah":{"name":"nhung","score":"123"}}
fun String.formatNonstrictJsonBuilder(): String {
    var formatStr = this.replace(", null", "")
    formatStr = formatStr.replace("=", "\":\"")
    formatStr = formatStr.replace(", ", "\", \"")
    formatStr = formatStr.replace("}\",", "}, ")
    formatStr = formatStr.replace("}", "\"}")
    formatStr = formatStr.replace("{", "{\"")
    formatStr = formatStr.replace("\"{\"", "{\"")
    formatStr = formatStr.replace("}\"}", "}}")
    return formatStr
}

fun Map<String, Any>.parseToSessionStatus(): SessionStatus {
    val session = SessionStatus()
    val questionId = this[PlayerInSessionViewModel.QUESTION_ID]
    (questionId is String && questionId.isNotBlank()).let {
        session.questionID = questionId as String
    }
    val status = this[STATE]
    if (status is String && status != null) {
        session.status = status
    }
    return session
}

fun Map<String, Any>.parseToUser(sessionId: String, displayName: String): User {
    val user = User(idSession = sessionId, displayName = displayName)
    (this[IMAGE] is String).let {
        user.image = this[IMAGE] as String
    }
    (this[SCORE] is Long).let {
        user.score = (this[SCORE] as Long).toInt()
    }
    return user
}

fun prepareUserMap(user: User): MutableMap<String, Any> {
    val userMap = mutableMapOf<String, Any>(
        IMAGE to user.image,
        USER_STATUS to user.status,
        SCORE to user.score
    )
    val timeJoined = user.timeJoined
    if (timeJoined != null) userMap.put(TIME_JOINED, timeJoined)
    return userMap
}

fun prepareAnswer(questionIdx: Int): Map<String, Any> {
    return mutableMapOf<String, Any>(INDEX to questionIdx, TIME_ANSWER to timestampNow())
}

