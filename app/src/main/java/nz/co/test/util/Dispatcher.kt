package nz.co.test.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class AppDispatchers(
    val IO: CoroutineDispatcher = Dispatchers.IO,
    val Default: CoroutineDispatcher = Dispatchers.Default,
    val Main: CoroutineDispatcher = Dispatchers.Main
)