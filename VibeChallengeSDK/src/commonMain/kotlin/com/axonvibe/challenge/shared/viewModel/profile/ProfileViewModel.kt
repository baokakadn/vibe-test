package com.axonvibe.challenge.shared.viewModel.profile

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.enum.PlayerStatus
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.data.preferences.SessionPreference
import com.axonvibe.challenge.shared.di.KodeinInjector
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.logout.LogoutProfileRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.logout.LogoutProfileUseCase
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.save.GetProfileFromStoreRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.save.GetProfileFromStoreUseCase
import com.axonvibe.challenge.shared.util.SESSION_NOT_FOUND
import com.axonvibe.challenge.shared.util.USER_STATUS
import com.axonvibe.challenge.shared.util.launchSilent
import com.axonvibe.challenge.shared.viewModel.playerInSession.PlayerInSessionViewModel
import com.axonvibe.challenge.shared.viewModel.profile.state.*
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class ProfileViewModel() : ViewModel() {
    var profileLiveData = MutableLiveData<ProfileState>(LoadingGetProfileState())
    val firebaseCloud = FirebaseCloud()
    val sessionPref = SessionPreference()
    var sessionStatus = MutableLiveData<String>("")

    val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    val getProfileUserCase by KodeinInjector.instance<GetProfileUseCase>()
    val getSavedProfileUserCase by KodeinInjector.instance<GetProfileFromStoreUseCase>()
    val logoutProfileUserCase by KodeinInjector.instance<LogoutProfileUseCase>()
    val job: Job = Job()
    val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun actionIfUserInactive() = launchSilent(coroutineContext, exceptionHandler, job) {
        val sessionId = sessionPref.getSessionId()
        if (sessionId != SESSION_NOT_FOUND) {
            val request = GetProfileRequest(PlayerInSessionViewModel.GET_FROM_LOCAL_CODE)
            val profileRes = getProfileUserCase.execute(request)
            if (profileRes is Response.Success) {
                val user = profileRes.data.user
                val userMap = firebaseCloud.getPlayerInSession(sessionId, user)
                if (userMap?.get(USER_STATUS) == PlayerStatus.INACTIVE_USER.status) {
                    user.status = PlayerStatus.ACTIVE_USER.status
                    val res = firebaseCloud.updateUser(sessionId, user)
                    if (res is Response.Success) {
                        sessionStatus.postValue(res.data)
                    }
                }
            }
        }
    }

    fun getProfile(code: String) = launchSilent(coroutineContext, exceptionHandler, job) {
        profileLiveData.postValue(LoadingGetProfileState())
        val request = GetProfileRequest(code)
        val response = getProfileUserCase.execute(request)
        if (response is Response.Success) {
            profileLiveData.postValue(SuccessGetProfileState(response))
        } else {
            profileLiveData.postValue(ErrorGetProfileState(response))
        }
    }

    fun getSavedProfile() = launchSilent(coroutineContext, exceptionHandler, job) {
        profileLiveData.postValue(LoadingGetProfileState())
        val request = GetProfileFromStoreRequest()
        val response = getSavedProfileUserCase.execute(request)
        processProfileResponse(response)
    }

    fun logoutProfile() = launchSilent(coroutineContext, exceptionHandler, job) {
        val request = LogoutProfileRequest()
        val response = logoutProfileUserCase.execute(request)
        if (response is Response.Success) {
            profileLiveData.postValue(LogoutProfileState())
        }
    }

    fun processProfileResponse(response: Response<Info>) {
        if (response is Response.Success) {
            profileLiveData.postValue(SuccessGetProfileState(response))
        } else {
            profileLiveData.postValue(ErrorGetProfileState(response))
        }
    }
}
