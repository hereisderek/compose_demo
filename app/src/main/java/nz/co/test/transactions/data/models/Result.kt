package nz.co.test.transactions.data.models

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    companion object {
        fun <R>of(data: R) = Success(data)
        fun error(exception: Exception) = Error(exception)
    }
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}
