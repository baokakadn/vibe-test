package com.axonvibe.challenge.shared.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun launchSilent(
    context: CoroutineContext = Dispatchers.Main,
    exceptionHandler: CoroutineExceptionHandler,
    job: Job,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = run {
    val coroutineScope = CoroutineScope(context + job + exceptionHandler)
    coroutineScope.launch(context, start, block)
}