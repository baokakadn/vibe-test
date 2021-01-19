package com.axonvibe.challenge.shared.domain.usecase.base

interface BaseRequest {
    fun validate(): Boolean
}