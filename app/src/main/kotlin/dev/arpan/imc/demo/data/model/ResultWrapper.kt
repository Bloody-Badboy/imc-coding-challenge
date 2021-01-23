package dev.arpan.imc.demo.data.model

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val throwable: Throwable? = null) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$value]"
            is Error -> "GenericError[error=$throwable]"
        }
    }
}
