package kinyua.vivian.domain.model

data class MissionReport(
    val id: String,
    val spacecraftInfo: SpacecraftInfo,
    val startedAtMs: Long,
    val completedAtMs: Long? = null,
    val checkResults: List<CheckResult> = emptyList()
) {
    val totalChecks: Int get() = checkResults.size
    val nominalChecks: Int get() = checkResults.count { it.isNominal }
    val anomalyChecks: Int get() = checkResults.count { it.isAnomaly }
    val standbyChecks: Int get() = checkResults.count { it.isStandby }

    val healthScore: Int = if (totalChecks == 0) 0
        else ((nominalChecks.toFloat() / totalChecks) * 100).toInt()

    val duration: Long? get() = completedAtMs?.let { it - startedAtMs }

    val isAllNominal: Boolean get() = checkResults.isNotEmpty() && anomalyChecks == 0 && standbyChecks == 0

    fun resultForCheck(check: SystemCheck): CheckResult? {
        return checkResults.find { it.check == check }
    }
}
