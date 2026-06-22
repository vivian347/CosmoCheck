package kinyua.vivian.domain.model

enum class CheckStatus {
    PENDING,
    IN_PROGRESS,
    NOMINAL, //pass
    ANOMALY, //fail
    STANDBY
}