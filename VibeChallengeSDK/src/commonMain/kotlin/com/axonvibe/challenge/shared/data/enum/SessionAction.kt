package com.axonvibe.challenge.shared.data.enum

enum class SessionAction(val value: String) {
    GAME_STARTED("Game started"),
    WAITING_FOR_ANSWER_QUESTION("Answering"),
    ANSWERING_QUESTION("Answering question"),
    CALCULATING("Calculating"),
    WAITING_RESULT("Waiting for answer"),
    RESULT("Resulting"),
    WRONG_ANSWER("wrong answer"),
    RIGHT_ANSWER("right answer"),
    END_GAME("end")
}