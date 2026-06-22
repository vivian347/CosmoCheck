package kinyua.vivian.common

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()

    data object Loading: Result<Nothing>()

    data class Failure(val failure: CosmoFailure): Result<Nothing>()

    val isSuccess: Boolean
        get() = this is Success
    val isLoading: Boolean
        get() = this is Loading
    val isFailure: Boolean
        get() = this is Failure

    fun getOrNull(): T? = (this as? Success)?.data

    fun getOrThrow(): T = (this as? Success)?.data ?: throw NoSuchElementException("Result is not Success: $this")

    fun getOrDefault(default: @UnsafeVariance T): T = (this as? Success)?.data ?: default


    fun <R> map(transform: (T) -> R): Result<R> = when(this) {
        is Failure -> this
        Loading -> Loading
        is Success -> Success(transform(data))
    }

    fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when(this) {
        is Failure -> this
        Loading -> Loading
        is Success -> transform(data)
    }

    fun recover(fallback:(CosmoFailure) -> Result<@UnsafeVariance T>): Result<T> =
        if (this is Failure) fallback(failure) else this

    fun onSuccess(action: (T) -> Unit): Result<T> = apply {
        if (this is Success) action(data)
        return this
    }

    fun onFailure(action: (CosmoFailure) -> Unit): Result<T> {
        if (this is Failure) action(failure)
        return this
    }

}