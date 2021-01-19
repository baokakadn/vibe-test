package com.axonvibe.challenge.shared.domain.usecase.proflieRepository.logout

import com.axonvibe.challenge.shared.domain.usecase.base.BaseRequest

class LogoutProfileRequest : BaseRequest {
    override fun validate(): Boolean {
        return true
    }
}