package com.axonvibe.challenge.shared.domain.usecase.base

import com.axonvibe.challenge.shared.base.Response

abstract class BaseUseCase<R : BaseRequest, T> {
    var request: R? = null
    suspend fun execute(request: R? = null): Response<T> {
        this.request = request
        val validated = request?.validate() ?: true
        if (validated) return run()
        return Response.Error(IllegalArgumentException())
    }

    abstract suspend fun run(): Response<T>
}