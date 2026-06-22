package kinyua.vivian.common

fun <T> success(data: T): Result<T> = Result.Success(data)

fun failure(failure: CosmoFailure): Result<Nothing> = Result.Failure(failure)

fun failure(cause: Throwable): Result<Nothing> = Result.Failure(CosmoFailure.Unknown(cause.message ?: "Unknown error", cause))

inline fun <T> cosmoRunCatching(block: () -> T): Result<T> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Failure(CosmoFailure.Unknown(e.message ?: "Unknown error", e))
    }
}