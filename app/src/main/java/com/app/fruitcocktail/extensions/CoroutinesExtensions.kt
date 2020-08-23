package com.app.fruitcocktail.extensions

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchExt(
    job: Job?,
    coroutineContext: CoroutineContext = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): Job {
    job?.cancel()
    return launch(coroutineContext) {
        block()
    }
}