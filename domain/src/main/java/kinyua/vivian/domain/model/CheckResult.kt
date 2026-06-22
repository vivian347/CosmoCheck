package kinyua.vivian.domain.model

data class CheckResult(
    val check: SystemCheck,
    val status: CheckStatus,
    val details: String = "",
    val timeStampMs: Long = System.currentTimeMillis()
) {

    init {
        require(status != CheckStatus.PENDING) {
            "A completed CheckResult cannot have a PENDING status."
        }

        require(status != CheckStatus.IN_PROGRESS) {
            "A completed CheckResult cannot have an IN_PROGRESS status."
        }
    }

    val isNominal: Boolean
        get() = status == CheckStatus.NOMINAL
    val isAnomaly: Boolean
        get() = status == CheckStatus.ANOMALY
    val isStandby: Boolean
        get() = status == CheckStatus.STANDBY

    companion object {
        fun nominal(check: SystemCheck, details: String = ""): CheckResult {
            return CheckResult(check, CheckStatus.NOMINAL, details)
        }

        fun anomaly(check: SystemCheck, details: String): CheckResult {
            return CheckResult(check, CheckStatus.ANOMALY, details)
        }

        fun standby(check: SystemCheck, details: String): CheckResult {
            return CheckResult(check, CheckStatus.STANDBY, details)
        }
    }

}
