package kinyua.vivian.testing.fake

import kinyua.vivian.common.DEFAULT_TIMEOUT_MS
import kinyua.vivian.common.Result
import kinyua.vivian.domain.model.CheckResult
import kinyua.vivian.domain.model.SystemCheck
import kinyua.vivian.domain.tester.HardwareTester
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class FakeHardwareTester(
    override val systemCheck: SystemCheck,
    override val missionBriefing: String = "Fake briefing for ${systemCheck.displayName}",
    override val requiredPermissions: List<String> = emptyList(),
    override val timeoutMs: Long = DEFAULT_TIMEOUT_MS,
    override val requiresManualConfirmation: Boolean = false,
    private val result: CheckResult = CheckResult.nominal(systemCheck),
    private val throwable: Throwable? = null,
    private val delayMs: Long = 0L,
): HardwareTester {
    var runCount: Int = 0
        private set

    override suspend fun run(): CheckResult {
        runCount++
        if (delayMs > 0) delay(delayMs.milliseconds)
        throwable?.let { throw it }
        return result
    }

    companion object {
        fun nominal(
            check: SystemCheck,
            details: String = "",
            delayMs: Long = 0L,
        ) = FakeHardwareTester(
            systemCheck = check,
            result = CheckResult.nominal(check, details),
            delayMs = delayMs,
        )

        fun anomaly(
            check: SystemCheck,
            reason: String = "Fake anomaly",
            delayMs: Long = 0L,
        ) = FakeHardwareTester(
            systemCheck = check,
            result      = CheckResult.anomaly(check, reason),
            delayMs     = delayMs,
        )

        fun standby(
            check: SystemCheck,
            reason: String = "Fake standby",
        ) = FakeHardwareTester(
            systemCheck = check,
            result      = CheckResult.standby(check, reason),
        )

        fun throwing(
            check: SystemCheck,
            throwable: Throwable = RuntimeException("Fake exception from ${check.displayName}"),
        ) = FakeHardwareTester(
            systemCheck = check,
            throwable   = throwable,
        )

        fun timeout(
            check: SystemCheck,
            timeoutMs: Long = DEFAULT_TIMEOUT_MS,
        ) = FakeHardwareTester(
            systemCheck = check,
            timeoutMs   = timeoutMs,
            delayMs     = timeoutMs + 5_000L,
        )

        fun allNominal(): Map<SystemCheck, FakeHardwareTester> =
            SystemCheck.entries.associateWith { nominal(it) }

    }
}