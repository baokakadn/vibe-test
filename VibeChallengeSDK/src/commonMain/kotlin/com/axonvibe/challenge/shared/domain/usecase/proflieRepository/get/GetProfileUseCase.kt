package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.TOKEN_KEY
import com.axonvibe.challenge.shared.data.network.SlackAPI
import com.axonvibe.challenge.shared.data.preferences.SlackPreference
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.usecase.base.BaseUseCase

class GetProfileUseCase(val repository: SlackPreference, val slackAPI: SlackAPI) :
    BaseUseCase<GetProfileRequest, Info>() {
    override suspend fun run(): Response<Info> {
        if (repository.hasKey(TOKEN_KEY)) {
            return repository.getProfile()
        }
        val response = slackAPI.getProfileByCode(request!!.code)
        if (response is Response.Success) {
            repository.storeProfile(response.data)
        }
        return response
    }

}