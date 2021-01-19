package com.axonvibe.challenge.shared.viewModel.profile.state

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.domain.model.Info

sealed class ProfileState {
    abstract val response: Response<Info>?
}

data class SuccessGetProfileState(override val response: Response<Info>) : ProfileState()
data class LoadingGetProfileState(override val response: Response<Info>? = null) : ProfileState()
data class ErrorGetProfileState(override val response: Response<Info>) : ProfileState()
data class LogoutProfileState(override val response: Response<Info>? = null) : ProfileState()
data class GetSavedProfileState(override val response: Response<Info>? = null) : ProfileState()
