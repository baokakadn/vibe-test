package com.axonvibe.challenge.shared.data.network

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.configutation.ACCESS_TOKEN_URL
import com.axonvibe.challenge.shared.configutation.USER_INFO_URL
import com.axonvibe.challenge.shared.domain.model.Info
import com.axonvibe.challenge.shared.domain.model.Profile
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

class SlackAPI {
    private val httpClient = HttpClient ()

    suspend fun getProfileByCode(code: String): Response<Info> {
        try {
            val jsonProfile = httpClient.get<String>(ACCESS_TOKEN_URL + code)
            val profile = Json.nonstrict.parse(Profile.serializer(), jsonProfile)

            val jsonUser = httpClient.get<String>(USER_INFO_URL + profile.accessToken)
            val info = Json.nonstrict.parse(Info.serializer(), jsonUser)
            info.user.accessToken = profile.accessToken

            return Response.Success(info)
        } catch (ex: Exception) {
            return Response.Error(ex)
        }
    }

}