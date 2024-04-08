package com.example.imagefeed.domain.mapper

import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import com.fasterxml.jackson.databind.JsonMappingException
import kotlinx.coroutines.TimeoutCancellationException
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class NetworkResponseMapper @Inject constructor() {
    suspend fun <Data> asResponse(
        request: suspend () -> Data,
    ) : Response<Data, NetworkError> {
        return try {
            Response.Success(request())
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> Response.Failure(NetworkError.UnauthorizedError(e))
                else -> Response.Failure(NetworkError.GenericError(e))
            }
        } catch (e: JsonMappingException) {
            Response.Failure(NetworkError.JsonMappingError(e))
        } catch (e: IOException) {
            Response.Failure(NetworkError.ConnectionError(e))
        } catch (e: TimeoutCancellationException) {
            Response.Failure(NetworkError.ConnectionError(e))
        }
    }
}