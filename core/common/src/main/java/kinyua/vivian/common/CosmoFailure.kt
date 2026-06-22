package kinyua.vivian.common

sealed class CosmoFailure(
    open val message: String,
    open val cause: Throwable? = null,
) {
    data class PermissionDenied(
        val permission: String,
        override val message: String = "Permission denied for $permission",
    ) : CosmoFailure(message)

    data class HardwareNotPresent(
        val component: String,
        override val message: String = "Hardware not present: $component",
    ): CosmoFailure(message)

    data class Timeout(
        val timeoutMs: Long,
        override val message: String = "Operation timed out after $timeoutMs ms",
    ): CosmoFailure(message)

    data class DatabaseError(
        override val message: String,
        override val cause: Throwable? = null,
    ): CosmoFailure(message, cause)

    data class Unknown(
        override val message: String = "An unknown error occurred",
        override val cause: Throwable? = null,
    ): CosmoFailure(message, cause)
}