package kinyua.vivian.common

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    //for CPU intensive work eg sorting, filtering, JSON parsing
    val default: CoroutineDispatcher

    //for IO work eg network calls, database operations, file operations, android system service calls
    val io: CoroutineDispatcher

    // for main thread work eg updating UI, collecting flows, etc
    val main: CoroutineDispatcher

    //doesn't re-dispatch if already on main thread. Maps to [Dispatchers.Main.immediate]
    val mainImmediate: CoroutineDispatcher

    // for unconfined work eg testing, or work that doesn't need to be confined to a specific thread
    val unconfined: CoroutineDispatcher

}