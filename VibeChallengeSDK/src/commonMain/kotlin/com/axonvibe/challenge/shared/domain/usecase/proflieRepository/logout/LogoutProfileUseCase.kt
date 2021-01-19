package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.logout

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.preferences.SlackPreference
import com.axonvibe.challenge.shared.domain.usecase.base.BaseUseCase

class LogoutProfileUseCase(val preferences: SlackPreference) :
    BaseUseCase<LogoutProfileRequest, Int>() {
    override suspend fun run(): Response<Int> {
        preferences.deleteAll()
        return Response.Success(1)
    }
}