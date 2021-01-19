package com.axonvibe.challenge.shared.viewModel.playerInSession

import co.touchlab.firebase.firestore.*
import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.enum.PlayerStatus
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.data.preferences.SessionPreference
import com.axonvibe.challenge.shared.data.preferences.UserPreference
import com.axonvibe.challenge.shared.di.KodeinInjector
import com.axonvibe.challenge.shared.util.launchSilent
import com.axonvibe.challenge.shared.domain.model.SessionStatus
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.util.USER_STATUS
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class PlayerInSessionViewModel() : ViewModel() {
    var actionStatus = MutableLiveData(PlayerStatus.HAVE_NOT_JOINED.status)
    val firebaseCloud = FirebaseCloud()
    lateinit var response: Response<String>
    val sessionPref = SessionPreference()
    val userPreference = UserPreference()

    // ASYNC - COROUTINES
    val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    val getProfileUserCase by KodeinInjector.instance<GetProfileUseCase>()

    fun playerJoinSession(qrCode: String) =
        launchSilent(coroutineContext, exceptionHandler, job) {
            actionStatus.postValue("")
            val idSession = qrCode.getSessionIDByQRCode()
            val secretKey = qrCode.getSecretKeyByQRCode()
            val session = firebaseCloud.getSessionIfValid(idSession)
            if (session != null && secretKey == session[SECRET_KEY]) {
                processWhenSessionValid(idSession)
            } else {
                response = Response.Success(PlayerStatus.JOINED_INVALID_SESSION.status)
            }
            processResponse(response)
        }

    suspend fun processWhenSessionValid(idSession: String) {
        val request = GetProfileRequest(GET_FROM_LOCAL_CODE)
        val profileRes = getProfileUserCase.execute(request)
        if (profileRes is Response.Success) {
            val user = profileRes.data.user
            user.timeJoined = timestampNow()
            val playerInSession = firebaseCloud.getPlayerInSession(idSession, user)
            response =
                if (playerInSession == null || playerInSession[USER_STATUS] == PlayerStatus.INACTIVE_USER.status) {
                    SessionPreference().saveSession(SessionStatus(id = idSession))
                    userPreference.saveUser(user)
                    firebaseCloud.createUser(idSession, user)
                } else {
                    Response.Success(PlayerStatus.PLAYER_ALREADY_IN_SESSION.status)
                }
        }
    }

    fun processResponse(response: Response<String>) {
        if (response is Response.Success) {
            actionStatus.postValue(response.data)
        } else {
            actionStatus.postValue(ERROR_RESULT)
        }
    }

    fun String.getSessionIDByQRCode(): String {
        return if (indexOf(DIVIDER_CODE) == -1) {
            ERROR_RESULT
        } else {
            substring(0, indexOf(DIVIDER_CODE))
        }
    }

    fun String.getSecretKeyByQRCode(): String {
        return this.substring(this.indexOf(DIVIDER_CODE) + 1)
    }

    suspend fun ensureSessionIsPlaying(idSession: String): Boolean {
        val sessionState = firebaseCloud.sessionStateRef(idSession)
        return sessionState.suspendGet().data_() != null
    }

    companion object {
        const val ERROR_RESULT = "error"
        const val QUESTION_ID = "questionID"
        const val DIVIDER_CODE = " "
        const val GET_FROM_LOCAL_CODE = ""
        const val SECRET_KEY = "secretKey"
    }
}

