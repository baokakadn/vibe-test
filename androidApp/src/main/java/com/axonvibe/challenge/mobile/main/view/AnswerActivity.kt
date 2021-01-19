package com.axonvibe.challenge.mobile.main.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.util.setSafeOnClickListener
import com.axonvibe.challenge.shared.data.enum.SessionAction
import com.axonvibe.challenge.shared.viewModel.answerQuestion.AnswerQuestionVM
import kotlinx.android.synthetic.main.activity_answer.*

class AnswerActivity : AppCompatActivity(R.layout.activity_answer) {
    // VIEW MODEL
    private lateinit var answerQuestionVM: AnswerQuestionVM

    var actionStatus = SessionAction.WAITING_FOR_ANSWER_QUESTION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        configViewModel()
    }

    private fun initView() {
        val blueButton = btn_answer_blue_answer_activity
        val greenButton = btn_answer_green_answer_activity
        val yellowButton = btn_answer_yellow_answer_activity
        val redButton = btn_answer_red_answer_activity

        blueButton.setSafeOnClickListener {
            disableButtons(greenButton, yellowButton, redButton)
            answerQuestion(ANSWER_A)
            showAlertDialog()
        }

        greenButton.setSafeOnClickListener {
            disableButtons(blueButton, yellowButton, redButton)
            answerQuestion(ANSWER_B)
            showAlertDialog()
        }
        yellowButton.setSafeOnClickListener {
            disableButtons(greenButton, blueButton, redButton)
            answerQuestion(ANSWER_C)
            showAlertDialog()
        }
        redButton.setSafeOnClickListener {
            disableButtons(greenButton, yellowButton, blueButton)
            answerQuestion(ANSWER_D)
            showAlertDialog()
        }
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
        if (isActionValid(it, SessionAction.RIGHT_ANSWER)) {
            correctPhrase()
        } else if (isActionValid(it, SessionAction.WRONG_ANSWER)) {
            wrongPhrase()
        }
    }

    fun wrongPhrase() {
        actionStatus = SessionAction.WRONG_ANSWER
        goToResultActivity(AnswerIncorrectActivity::class.java)
    }

    fun correctPhrase() {
        actionStatus = SessionAction.RIGHT_ANSWER
        goToResultActivity(AnswerCorrectActivity::class.java)
    }

    fun goToResultActivity(resultClass: Class<out AppCompatActivity>) {
        val intent = Intent(this, resultClass)
        startActivity(intent)
        finish()
    }

    fun endGame() {
        val intent = Intent(this, SuccessAuthActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        }
        startActivity(intent)
        finish()
    }

    fun isActionValid(action: SessionAction, validAction: SessionAction): Boolean {
        return validAction == action && actionStatus != validAction
    }


    fun answerQuestion(answerIdx: Int) {
        answerQuestionVM.answerQuestion(answerIdx)
    }

    fun disableButtons(button1: Button, button2: Button, button3: Button) {
        button1.isEnabled = false
        button1.setBackgroundColor(Color.GRAY)
        button2.isEnabled = false
        button2.setBackgroundColor(Color.GRAY)
        button3.isEnabled = false
        button3.setBackgroundColor(Color.GRAY)
    }

    private fun showAlertDialog() {
        val alertDialog = WaitingQuestionDialogFragment()
        alertDialog.isCancelable = false
        alertDialog.show(supportFragmentManager, WaitingQuestionDialogFragment.TAG)
    }

    override fun onBackPressed() {
    }

    companion object {
        const val ANSWER_A = 0
        const val ANSWER_B = 1
        const val ANSWER_C = 2
        const val ANSWER_D = 3
    }
}
