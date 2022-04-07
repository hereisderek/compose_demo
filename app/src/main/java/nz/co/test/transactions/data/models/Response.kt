package nz.co.test.transactions.data.models

sealed class Response<out R> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val exception: Exception) : Response<Nothing>()
    data class Loading<out T>(val data: T? = null) : Response<T>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading[partial data=$data]"
        }
    }

    fun <T>map(mapping: ((R)->T)) : Response<T> = when(this){
        is Success -> Success(mapping.invoke(data))
        is Error -> Error(exception)
        is Loading -> Loading(if (data != null) mapping.invoke(data) else null)
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: Exception) = Error(exception)
        fun <T> loading(data: T? = null) = Loading(data)
        val loading get() = Loading<Nothing>()
    }
}