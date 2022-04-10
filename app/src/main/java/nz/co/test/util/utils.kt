package nz.co.test.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


val Number.twoDecimalString : String get() = "%.02f".format(this)

@Composable
fun <T> rememberFlow(
    flow: Flow<T>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
): Flow<T> {
    return remember(key1 = flow, key2 = lifecycleOwner) {
        flow.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }
}

@Composable
fun <R> Flow<R>.lifecycleAwareFlowAsState(
    initialValue: R,
    context: CoroutineContext = EmptyCoroutineContext,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) : State<R> = rememberFlow(flow = this).collectAsState(initial = initialValue)


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun <R> StateFlow<R>.lifecycleAwareFlowAsState(
    context: CoroutineContext = EmptyCoroutineContext,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) : State<R> = rememberFlow(flow = this).collectAsState(initial = value)

val threadName: String get() = Thread.currentThread().name