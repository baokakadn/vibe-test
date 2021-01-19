package com.axonvibe.challenge.shared.viewModel.answerQuestion

import co.touchlab.firebase.firestore.addSnapshotListener_
import co.touchlab.firebase.firestore.data_
import com.axonvibe.challenge.shared.data.enum.SessionAction
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.di.KodeinInjector
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.util.launchSilent
import com.axonvibe.challenge.shared.util.parseToSessionStatus
import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.domain.usecase.session.SessionUseCase
import com.axonvibe.challenge.shared.domain.usecase.user.UserUseCase
import com.axonvibe.challenge.shared.data.preferences.SessionPreference
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class AnswerQuestionVM() : ViewModel() {
    val firebaseCloud = FirebaseCloud()

    // USECASE
    val userUseCase = UserUseCase()
    val sessionUseCase = SessionUseCase()

    // ASYNC - COROUTINES
    val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    // LIVE DATA
    val sessionLiveData = MutableLiveData(SessionStatus())
    val playerLiveData = MutableLiveData(User())
    val actionStatus = MutableLiveData(SessionAction.GAME_STARTED)

    fun listenToGame() {
        observerQuestion()
        val sessionId = SessionPreference().getSessionId()
        listenToSession(sessionId)
    }

    fun observerQuestion() =
        launchSilent(coroutineContext, exceptionHandler, job) {
            val sessionId = SessionPreference().getSessionId()
            val user = userUseCase.getUserStatus(sessionId)
            playerLiveData.postValue(user)
            val session = sessionUseCase.getSessionStatus(sessionId)
            sessionLiveData.postValue(session)
        }

    fun listenToSession(sessionId: String) {
        val sessionRef = firebaseCloud.sessionStateRef(sessionId)
        sessionRef.addSnapshotListener_(listener = { snapshot, e ->
            try {
                val data = snapshot?.data_()
                if (data != null) {
                    val session = (data as Map<String, Any>).parseToSessionStatus()
                    when (session.status) {
                        // calculating
                        SessionAction.CALCULATING.value ->
                            actionStatus.postValue(SessionAction.CALCULATING)

                        // received result
                        SessionAction.RESULT.value -> handleResult()

                        // loading question
                        SessionAction.WAITING_FOR_ANSWER_QUESTION.value ->
                            actionStatus.postValue(SessionAction.WAITING_FOR_ANSWER_QUESTION)

                        // game end
                        else -> actionStatus.postValue(SessionAction.END_GAME)
                    }
                }
            } catch (e: Exception) {
                print(e.message)
            }
        })
    }

    fun handleResult() =
        launchSilent(coroutineContext, exceptionHandler, job) {
            val user = userUseCase.getUserStatus(playerLiveData.value.idSession)
            if (user.score != playerLiveData.value.score && actionStatus.value != SessionAction.RIGHT_ANSWER) {
                actionStatus.postValue(SessionAction.RIGHT_ANSWER)
                playerLiveData.postValue(user)
            } else if (actionStatus.value != SessionAction.WRONG_ANSWER && actionStatus.value != SessionAction.RIGHT_ANSWER) {
                actionStatus.postValue(SessionAction.WRONG_ANSWER)
            }
        }

    fun answerQuestion(questionIndex: Int) {
        val user = playerLiveData.value
        actionStatus.postValue(SessionAction.ANSWERING_QUESTION)
        firebaseCloud.userAnswerQuestion(user, sessionLiveData.value.questionID, questionIndex)
    }
}