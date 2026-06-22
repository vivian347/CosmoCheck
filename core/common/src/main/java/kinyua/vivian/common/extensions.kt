package kinyua.vivian.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


// Flow extensions for Result handling
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this
    .map<T, Result<T>> { Result.Success(it) }
    .onStart { emit(Result.Loading) }
    .catch { emit(Result.Failure(CosmoFailure.Unknown(it.message ?: "Flow error", it))) }

fun <T, R> Flow<Result<T>>.mapResult(
    transform: (T) -> R,
): Flow<Result<R>> = map { it.map(transform) }

// Coroutine Extensions
suspend fun <T> withCosmoTimeout(
    timeoutMs: Long,
    block: suspend () -> T,
): Result<T> = withTimeoutOrNull(timeoutMs) {
    Result.Success(block()) as Result<T>
} ?: Result.Failure(CosmoFailure.Timeout(timeoutMs))

// Collection Extensions
fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean = !this.isNullOrEmpty()

fun <T> List<T>.splitBy(predicate: (T) -> Boolean): Pair<List<T>, List<T>> = partition(predicate)

fun <T, K> List<T>.groupByOrdered(keySelector: (T) -> K): LinkedHashMap<K, List<T>> {
    val map = LinkedHashMap<K, MutableList<T>>()
    for (element in this) {
        val key = keySelector(element)
        val list = map.getOrPut(key) { mutableListOf() }.add(element)
    }
    @Suppress("UNCHECKED_CAST")
    return map as LinkedHashMap<K, List<T>>
}

// String extensions
fun String.blankToNull(): String? = if (isBlank()) null else this

fun String.truncate(maxLength: Int, ellipsis: String = "…"): String =
    if (length <= maxLength) this else take(maxLength - ellipsis.length) + ellipsis

// Timestamp extensions
private val REPORT_DATE_FORMAT = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
private val SHORT_DATE_FORMAT = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
private val TIME_FORMAT = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

fun Long.toReportDateString(): String = REPORT_DATE_FORMAT.format(Date(this))
fun Long.toShortDateString(): String = SHORT_DATE_FORMAT.format(Date(this))
fun Long.toTimeString(): String = TIME_FORMAT.format(Date(this))

fun Long.toDurationString(): String {
    val totalSeconds = this / 1_000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return when {
        minutes > 0 -> "${minutes}m ${seconds}s"
        else        -> "${seconds}s"
    }
}

// ID generation
fun newId(): String = UUID.randomUUID().toString()

//Int/Float extensions
fun Int.clampPercent(): Int = coerceIn(0, 100)
fun Float.clampFraction(): Float = coerceIn(0f, 1f)