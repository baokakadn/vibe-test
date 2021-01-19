package com.axonvibe.challenge.mobile.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.shared.data.enum.SessionAction
import com.axonvibe.challenge.shared.viewModel.answerQuestion.AnswerQuestionVM

class AnswerCorrectActivity : AppCompatActivity(R.layout.activity_answer_correct) {
    // VIEW MODEL
    private lateinit var answerQuestionVM: AnswerQuestionVM

    val actionStatus = SessionAction.WAITING_FOR_ANSWER_QUESTION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configViewModel()
    }

    private fun configViewModel() {
        answerQuestionVM = ViewModelProviders.of(this).get(AnswerQuestionVM::class.java)
        answerQuestionVM.listenToGame()
        observerQuestion()
    }

    private fun observerQuestion() {
        answerQuestionVM.actionStatus.addObserver {
            handleAction(it)
        }
    }

    private fun handleAction(it: SessionAction) {
        if (isActionValid(
                it,
                SessionAction.WAITING_FOR_ANSWER_QUESTION
            ) || it == SessionAction.END_GAME
        ) {
            goToAnswerPhrase()
        }
    }

    fun goToAnswerPhrase() {
        finish()
    }

    override fun onBackPressed() {
    }

    fun isActionValid(action: SessionAction, validAction: SessionAction): Boolean {
        return validAction == action && actionStatus != action
    }
}