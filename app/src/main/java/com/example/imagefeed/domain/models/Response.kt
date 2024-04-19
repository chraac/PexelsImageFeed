package com.example.imagefeed.domain.models


interface Error {
    val exception: Exception
}

sealed class NetworkError(override val exception: Exception) : Error {
    data class ConnectionError(override val exception: Exception) : NetworkError(exception)
    data class GenericError(override val exception: Exception) : NetworkError(exception)
    data class JsonMappingError(override val exception: Exception) : NetworkError(exception)
    data class UnauthorizedError(override val exception: Exception) : NetworkError(exception)
}

class NoDataError(override val exception: Exception) : Error

sealed class Response<D, out E : Error> {
    data class Success<D, E : Error>(val data: D) : Response<D, E>()
    data class Failure<D, E : Error>(val error: E) : Response<D, E>()

    fun mapSuccess(transform: (D) -> D): Response<D, E> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> this
        }
    }
}


