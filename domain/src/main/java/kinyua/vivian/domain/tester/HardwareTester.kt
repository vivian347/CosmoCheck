package kinyua.vivian.domain.tester

import kinyua.vivian.core.common.DEFAULT_TIMEOUT_MS
import kinyua.vivian.domain.model.CheckResult
import kinyua.vivian.domain.model.SystemCheck

interface HardwareTester {
    val systemCheck: SystemCheck

    val missionBriefing: String

    val requiredPermissions: List<String>
        get() = emptyList()

    val timeoutMs: Long
        get() = DEFAULT_TIMEOUT_MS

    val requiresManualConfirmation: Boolean
        get() = false

    suspend fun run(): CheckResult
}