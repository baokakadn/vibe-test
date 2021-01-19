package com.axonvibe.challenge.mobile.main.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.axonvibe.challenge.mobile.R
import com.axonvibe.challenge.mobile.main.view.AlertMessageDialogFragment.Companion.MESSAGE
import com.axonvibe.challenge.mobile.main.view.SuccessAuthActivity.Companion.SESSION_ID
import com.axonvibe.challenge.shared.data.enum.PlayerStatus
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.viewModel.waitingForStart.WaitForStartVM

class WaitingActivity : AppCompatActivity() {
    lateinit var waitForStartVM: WaitForStartVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)
        configViewModel()
    }

    private fun configViewModel() {
        waitForStartVM =
            ViewModelProviders.of(this).get(WaitForStartVM::class.java)
        val idSession = intent.getStringExtra(SESSION_ID)
        waitForStartVM.observeSession(idSession)
        waitForStartVM.sessionStatus.addObserver { processSessionStatus(it) }
        waitForStartVM.playerStatus.addObserver { getPlayerStatus(it) }
    }

    private fun getPlayerStatus(playerStatus: String) {
        if (playerStatus == PlayerStatus.PLAYER_HAS_BEEN_KICKED.status) {
            goToSuccessAuthScreen(PlayerStatus.PLAYER_HAS_BEEN_KICKED.status)
        } else if (playerStatus == PlayerStatus.ANOTHER_DEVICE_HAVE_JOINED.status) {
            goToSuccessAuthScreen(PlayerStatus.ANOTHER_DEVICE_HAVE_JOINED.status)
        }
    }

    fun goToSuccessAuthScreen(message: String) {
        val intent = Intent(this, SuccessAuthActivity::class.java)
        intent.putExtra(MESSAGE, message)
        startActivity(intent)
        finish()
    }

    private fun processSessionStatus(session: SessionStatus) {
        if (session.questionID.isNotEmpty()) {
            goToQuestionActivity()
        }
    }

    private fun goToQuestionActivity() {
        val intent = Intent(this, AnswerActivity::class.java)
            .apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
    }

    override fun onStart() {
        waitForStartVM.playerGoBack()
        super.onStart()
    }

    override fun onStop() {
        waitForStartVM.playerGoOut()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        waitForStartVM.sessionStatus.removeObserver { processSessionStatus(it) }
        waitForStartVM.playerStatus.removeObserver { getPlayerStatus(it) }
    }
}