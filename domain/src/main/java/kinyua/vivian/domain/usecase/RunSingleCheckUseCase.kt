package kinyua.vivian.domain.usecase

import kinyua.vivian.domain.model.SystemCheck
import kinyua.vivian.domain.orchestrator.LaunchEvent
import kinyua.vivian.domain.orchestrator.LaunchOrchestrator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RunSingleCheckUseCase @Inject constructor(
    private val orchestrator: LaunchOrchestrator
) {
    operator fun invoke(check: SystemCheck): Flow<LaunchEvent> = orchestrator.launch(listOf(check))

    fun submitConfirmation(check: SystemCheck, wasNominal: Boolean) {
        orchestrator.submitCrewConfirmation(check, wasNominal)
    }
}