package az.pashabank.data.errors

import az.pashabank.data.remote.error.ServerProblemDescription
import az.pashabank.starter.domain.exceptions.ErrorMapper
import az.pashabank.starter.domain.exceptions.NetworkError
import az.pashabank.starter.domain.exceptions.ServerError
import az.pashabank.starter.domain.exceptions.UnknownError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class RemoteErrors(val code: String) {
    UNEXPECTED_ERROR("error.unexpected")
}

class RemoteErrorMapper : ErrorMapper {

    override fun mapError(e: Throwable): Throwable = when (e) {
        is ClientRequestException -> mapHttpErrors(e)
        is ResponseException -> mapHttpErrors(e)
        is SocketException,
        is SocketTimeoutException,
        is UnknownHostException,
        is IOException
        -> NetworkError(e)
        else -> UnknownError(e)
    }

    private fun mapHttpErrors(error: ResponseException): Throwable {
        val description = try {
            // Try to parse error body if it's a ClientRequestException with cached response
            if (error is ClientRequestException) {
                // The error message often contains the response body
                val errorBody = error.message
                Json.decodeFromString<ServerProblemDescription>(errorBody)
            } else {
                null
            }
        } catch (ex: Throwable) {
            null
        } ?: ServerProblemDescription()

        return when (error.response.status.value) {
            401 -> ServerError.NotAuthorized(description.code, description.message)
            in 500..600 -> {
                ServerError.ServerIsDown(description.code, description.message)
            }
            else -> {
                when (description.code) {
                    RemoteErrors.UNEXPECTED_ERROR.code ->
                        ServerError.Unexpected(
                            description.code,
                            description.message
                        )
                    else -> ServerError.Unexpected(description.code, description.message)
                }
            }
        }
    }
}