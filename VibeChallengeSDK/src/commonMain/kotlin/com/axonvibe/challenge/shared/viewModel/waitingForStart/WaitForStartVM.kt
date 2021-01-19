package com.axonvibe.challenge.shared.viewModel.waitingForStart

import co.touchlab.firebase.firestore.*
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.data.preferences.SessionPreference
import com.axonvibe.challenge.shared.data.preferences.UserPreference
import com.axonvibe.challenge.shared.di.KodeinInjector
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.data.enum.PlayerStatus
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.util.*
import com.axonvibe.challenge.shared.viewModel.playerInSession.PlayerInSessionViewModel.Companion.GET_FROM_LOCAL_CODE
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class WaitForStartVM() : ViewModel() {
    val firebaseCloud = FirebaseCloud()
    val sessionStatus = MutableLiveData(INACTIVE_SESSION_STATUS)
    val getProfileUserCase by KodeinInjector.instance<GetProfileUseCase>()
    var playerStatus = MutableLiveData(PlayerStatus.PLAYER_ALREADY_IN_SESSION.status)
    val sessionPref = SessionPreference()
    val userPreference = UserPreference()

    // ASYNC - COROUTINES
    val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun observeSession(idSession: String) =
        launchSilent(coroutineContext, exceptionHandler, job) {
            playerStatus.postValue(idSession)
            waitForStartSession(idSession)
            playerGetKicked(idSession)
        }

    fun playerGoOut() = launchSilent(coroutineContext, exceptionHandler, job) {
        val infoRes = getPlayerFromLocal()
        if (infoRes is Response.Success) {
            val sessionId = playerStatus.value
            if (sessionId.isNotBlank()) {
                val user = infoRes.data.user
                user.status = PlayerStatus.INACTIVE_USER.status
                val res = firebaseCloud.updateUser(sessionId, user)
                sessionPref.saveSession(SessionStatus(id = sessionId))
                if (res is Response.Success) {
                    playerStatus.postValue(PlayerStatus.INACTIVE_USER.status)
                }
            }
        }
    }

    fun playerGoBack() = launchSilent(coroutineContext, exceptionHandler, job) {
        val sessionId = sessionPref.getSessionId()
        if (sessionId != SESSION_NOT_FOUND) {
            val request = GetProfileRequest(GET_FROM_LOCAL_CODE)
            val profileRes = getProfileUserCase.execute(request)
            if (profileRes is Response.Success) {
                val user = profileRes.data.user
                val playerInSession = firebaseCloud.getPlayerInSession(sessionId, user)
                if (playerInSession?.get(USER_STATUS) == PlayerStatus.INACTIVE_USER.status) {
                    user.status = PlayerStatus.ACTIVE_USER.status
                    firebaseCloud.updateUser(sessionId, user)
                    playerStatus.postValue(sessionId)
                }
            }
        }
    }

    suspend fun getPlayerFromLocal(): Response<Info> {
        val request = GetProfileRequest(GET_FROM_LOCAL_CODE)
        return getProfileUserCase.execute(request)
    }

    fun waitForStartSession(idSession: String) =
        launchSilent(coroutineContext, exceptionHandler, job) {
            firebaseCloud.sessionStateRef(idSession)
                .addSnapshotListener_ { snapshot, exception ->
                    try {
                        val data = snapshot?.data_()
                        data?.let {
                            if (isGameStarted(it)) {
                                val session = (data as Map<String, Any>).parseToSessionStatus()
                                session.id = idSession
                                sessionStatus.postValue(session)
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
        }

    fun playerGetKicked(idSession: String) =
        launchSilent(coroutineContext, exceptionHandler, job) {
            val request = GetProfileRequest(GET_FROM_LOCAL_CODE)
            val profileRes = getProfileUserCase.execute(request)
            if (profileRes is Response.Success) {
                val user = profileRes.data.user
                firebaseCloud.isPlayerExist(idSession, user)
                    .addSnapshotListener_ { snapshot, e ->
                        try {
                            val data = snapshot?.data_()
                            val timeJoined = userPreference.getTimeJoined()
                            if (data == null) {
                                playerStatus.postValue(PlayerStatus.PLAYER_HAS_BEEN_KICKED.status)
                                userPreference.removeSessionPref()
                            } else if (timeJoined != SESSION_NOT_FOUND && timeJoined != data[TIME_JOINED].toString()) {
                                playerStatus.postValue(PlayerStatus.ANOTHER_DEVICE_HAVE_JOINED.status)
                            }
                        } catch (e: Exception) {
                        }
                    }
            }
        }

    fun isGameStarted(data: Map<String, Any?>): Boolean {
        return data.size != 0
    }

    suspend fun String.ensureSessionIsPlaying(): Boolean {
        val sessionState = firebaseCloud.sessionStateRef(this)
        if (sessionState.suspendGet().data_() != null) {
            return true
        }
        return false
    }

    companion object {
        val INACTIVE_SESSION_STATUS: SessionStatus = SessionStatus()
    }
}

