package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get

import com.axonvibe.challenge.shared.domain.usecase.base.BaseRequest

class GetProfileRequest(val code: String) : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}