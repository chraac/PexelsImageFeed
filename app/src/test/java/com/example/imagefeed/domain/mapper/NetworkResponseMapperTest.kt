package com.example.imagefeed.domain.mapper

import com.example.imagefeed.domain.models.NetworkError
import com.example.imagefeed.domain.models.Response
import com.fasterxml.jackson.databind.JsonMappingException
import com.squareup.burst.BurstJUnit4
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import okio.IOException
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response as RetrofitResponse

@RunWith(BurstJUnit4::class)
class NetworkResponseMapperTest {
    private val subject = NetworkResponseMapper()

    @Test
    fun `given a successful response, when asResponse then response is mapped`(permutation: Permutations) =
        runTest {
            // When
            val actual = subject.asResponse(permutation.request)

            // Then
            assertEquals(permutation.expected, actual)
        }

    companion object {
        private val mockResponseBody = mock<ResponseBody>()
        private val mockHttpException401 = HttpException(
            RetrofitResponse.error<String>(
                401,
                mockResponseBody,
            )
        )
        private val mockHttpException404 = HttpException(
            RetrofitResponse.error<String>(
                404,
                mockResponseBody,
            )
        )
        private val mockJsonMappingException = mock<JsonMappingException>()
        private val mockIOException = mock<IOException>()
        private val mockTimeoutCancellationException = mock<TimeoutCancellationException>()

        @Suppress("unused")
        enum class Permutations(
            val request: suspend () -> String,
            val expected: Response<String, NetworkError>,
        ) {
            SUCCESS(
                request = { "mapped response" },
                expected = Response.Success("mapped response"),
            ),
            HTTP_401(
                request = {
                    throw mockHttpException401
                },
                expected = Response.Failure(NetworkError.UnauthorizedError(mockHttpException401)),
            ),
            HTTP_404(
                request = {
                    throw mockHttpException404
                },
                expected = Response.Failure(NetworkError.GenericError(mockHttpException404)),
            ),
            JSON_MAPPING_ERROR(
                request = { throw mockJsonMappingException },
                expected = Response.Failure(NetworkError.JsonMappingError(mockJsonMappingException)),
            ),
            IO_ERROR(
                request = { throw mockIOException },
                expected = Response.Failure(NetworkError.ConnectionError(mockIOException)),
            ),
            TIMEOUT_ERROR(
                request = { throw mockTimeoutCancellationException },
                expected = Response.Failure(
                    NetworkError.ConnectionError(
                        mockTimeoutCancellationException
                    )
                ),
            ),
        }
    }
}