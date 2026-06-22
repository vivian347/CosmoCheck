package kinyua.vivian.domain.orchestrator

import kinyua.vivian.domain.model.SystemCheck
import kotlinx.coroutines.flow.Flow

interface LaunchOrchestrator {

    fun launch(checks: List<SystemCheck>): Flow<LaunchEvent>

    fun submitCrewConfirmation(check: SystemCheck, wasNominal: Boolean)

}