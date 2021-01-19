package com.axonvibe.challenge.shared.domain.usecase.user

import com.axonvibe.challenge.shared.base.Response
import com.axonvibe.challenge.shared.data.firebaseCloud.FirebaseCloud
import com.axonvibe.challenge.shared.di.KodeinInjector
import com.axonvibe.challenge.shared.domain.model.User
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileRequest
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.util.parseToUser
import com.axonvibe.challenge.shared.viewModel.playerInSession.PlayerInSessionViewModel
import org.kodein.di.erased.instance

class UserUseCase {
    val getProfileUserCase by KodeinInjector.instance<GetProfileUseCase>()
    val firebaseCloud = FirebaseCloud()

    suspend fun getUserStatus(idSession: String): User {
        val profileReq = GetProfileRequest(PlayerInSessionViewModel.GET_FROM_LOCAL_CODE)
        val profileRes = getProfileUserCase.execute(profileReq)
        var user = User()
        if (profileRes is Response.Success) {
            user = profileRes.data.user
            user.idSession = idSession
            user = (firebaseCloud.getPlayerInSession(
                idSession,
                user
            ) as Map<String, Any>).parseToUser(idSession, user.displayName)
        }
        return user
    }
}