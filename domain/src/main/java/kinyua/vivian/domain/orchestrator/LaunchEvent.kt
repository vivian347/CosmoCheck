package kinyua.vivian.domain.orchestrator

import kinyua.vivian.domain.model.CheckResult
import kinyua.vivian.domain.model.SystemCheck

sealed class LaunchEvent {
    data class CheckStarted(
        val check: SystemCheck,
        val index: Int,
        val totalChecks: Int
    ) : LaunchEvent() {
        val progressFraction get() = index.toFloat() / totalChecks
    }

    data class CheckCompleted(
        val result: CheckResult
    ): LaunchEvent()

    data class CheckSkipped(
        val check: SystemCheck,
        val reason: String
    ): LaunchEvent()

    data object SequenceCompleted : LaunchEvent()

}