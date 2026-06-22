package kinyua.vivian.testing.rule

import kinyua.vivian.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(
    val testScheduler: TestCoroutineScheduler = TestCoroutineScheduler(),
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(testScheduler)
): DispatcherProvider {
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val main: CoroutineDispatcher
        get() = dispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = dispatcher
    override val unconfined: CoroutineDispatcher
        get() = dispatcher
}