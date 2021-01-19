package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.save

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.TOKEN_KEY
import com.axonvibe.challenge.shared.data.preferences.SlackPreference
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.usecase.base.BaseUseCase

class GetProfileFromStoreUseCase(val repository: SlackPreference) :
    BaseUseCase<GetProfileFromStoreRequest, Info>() {
    override suspend fun run(): Response<Info> {
        if (repository.hasKey(TOKEN_KEY)) {
            val response = repository.getProfile()
            return response
        }
        return Response.Error(IllegalArgumentException())
    }
}