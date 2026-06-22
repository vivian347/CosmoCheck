package kinyua.vivian.testing.fake

import kinyua.vivian.domain.model.CheckResult
import kinyua.vivian.domain.model.SystemCheck
import kinyua.vivian.domain.orchestrator.LaunchEvent
import kinyua.vivian.domain.orchestrator.LaunchOrchestrator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLaunchOrchestrator(
    private val results: Map<SystemCheck, CheckResult>? = null,
    private val shouldThrow: Throwable? = null
): LaunchOrchestrator {
    // Observation state
    var launchWasCalled: Boolean = false
        private set
    var lastChecksReceived: List<SystemCheck> = emptyList()
        private set
    var launchCallCount: Int = 0
        private set
    var lastConfirmationCheck: SystemCheck? = null
        private set
    var lastConfirmationWasNominal: Boolean? = null
        private set
    val emittedEvents: MutableList<LaunchEvent> = mutableListOf()

    // Manual-drive channel
    private val eventChannel = Channel<LaunchEvent>(Channel.UNLIMITED)

    fun emitEvent(event: LaunchEvent) {
        eventChannel.trySend(event)
    }


    override fun launch(checks: List<SystemCheck>): Flow<LaunchEvent> {
        launchWasCalled = true
        launchCallCount++
        lastChecksReceived = checks

        shouldThrow?.let { throw it }

        return if (results != null) {
            // automatic mode: emit events immediately
            flow {
                checks.forEachIndexed { index, check ->
                    val event = LaunchEvent.CheckStarted(check, index, checks.size)
                    emittedEvents.add(event)
                    emit(event)

                    val result = results[check] ?: CheckResult.nominal(check)
                    val completedEvent = LaunchEvent.CheckCompleted(result)
                    emittedEvents.add(completedEvent)
                    emit(completedEvent)
                }
                val doneEvent = LaunchEvent.SequenceCompleted
                emittedEvents.add(doneEvent)
                emit(doneEvent)
            }
        } else {
            // Manual mode: emit events from the channel
            flow {
                for (event in eventChannel) {
                    emittedEvents.add(event)
                    emit(event)
                    if (event is LaunchEvent.SequenceCompleted) break
                }
            }
        }
    }

    override fun submitCrewConfirmation(
        check: SystemCheck,
        wasNominal: Boolean
    ) {
        lastConfirmationCheck = check
        lastConfirmationWasNominal = wasNominal

    }

    fun reset() {
        launchWasCalled = false
        launchCallCount = 0
        lastChecksReceived = emptyList()
        lastConfirmationCheck = null
        lastConfirmationWasNominal = null
        emittedEvents.clear()
    }

}