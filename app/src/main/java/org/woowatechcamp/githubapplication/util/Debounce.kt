package org.woowatechcamp.githubapplication.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> debounce(
    timeMillis: Long = 500L,
    coroutineScope: CoroutineScope,
    block: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(timeMillis)
            block(it)
        }
    }
}
