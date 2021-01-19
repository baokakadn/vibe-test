package com.axonvibe.challenge.shared.di

import com.axonvibe.challenge.shared.ApplicationDispatcher
import com.axonvibe.challenge.shared.data.network.SlackAPI
import com.axonvibe.challenge.shared.data.preferences.SessionPreference
import com.axonvibe.challenge.shared.data.preferences.SlackPreference
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.get.GetProfileUseCase
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.logout.LogoutProfileUseCase
import com.axonvibe.challenge.shared.domain.usecase.proflieRepository.save.GetProfileFromStoreUseCase
import org.kodein.di.*
import org.kodein.di.erased.bind
import org.kodein.di.erased.instance
import org.kodein.di.erased.provider
import org.kodein.di.erased.singleton
import kotlin.coroutines.CoroutineContext

val KodeinInjector = Kodein {
    bind<CoroutineContext>() with provider { ApplicationDispatcher }

    bind<GetProfileUseCase>() with singleton { GetProfileUseCase(instance(), instance()) }
    bind<GetProfileFromStoreUseCase>() with singleton { GetProfileFromStoreUseCase(instance()) }
    bind<LogoutProfileUseCase>() with singleton { LogoutProfileUseCase(instance()) }

    bind<SlackPreference>() with provider { SlackPreference() }
    bind<SessionPreference>() with provider { SessionPreference() }
    bind<SlackAPI>() with provider { SlackAPI() }
}