package kinyua.vivian.domain.usecase

import kinyua.vivian.domain.model.SystemCheck
import kinyua.vivian.domain.orchestrator.LaunchEvent
import kinyua.vivian.domain.orchestrator.LaunchOrchestrator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunLauncSequenceUseCase @Inject constructor(
    private val orchestrator: LaunchOrchestrator
) {
    operator fun invoke(checks: List<SystemCheck>): Flow<LaunchEvent> = orchestrator.launch(checks)

    fun submitConfirmation(check: SystemCheck, wasNominal: Boolean) {
        orchestrator.submitCrewConfirmation(check, wasNominal)
    }

}