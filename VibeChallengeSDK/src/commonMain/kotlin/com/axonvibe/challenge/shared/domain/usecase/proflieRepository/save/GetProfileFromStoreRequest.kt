package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.save

import com.axonvibe.challenge.shared.domain.usecase.base.BaseRequest

class GetProfileFromStoreRequest() : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}