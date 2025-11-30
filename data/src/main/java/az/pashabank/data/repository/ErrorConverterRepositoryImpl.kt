package az.pashabank.data.repository

import az.pashabank.starter.data.remote.error.ServerProblemDescription
import az.pashabank.starter.domain.repository.ErrorConverterRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.readText
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ErrorConverterRepositoryImpl(
    val jsonSerializer: Json
) : ErrorConverterRepository {

    override fun getError(code: Int, identifier: String) {
        val errorBody = jsonSerializer.encodeToString(
            ServerProblemDescription.serializer(),
            ServerProblemDescription(identifier)
        )
        
        // Create a mock HttpResponse-like exception
        // Since we can't easily create a real ClientRequestException without a real HttpResponse,
        // we'll throw a custom exception that gets mapped by RemoteErrorMapper
        throw KtorTestException(code, errorBody, identifier)
    }
}

// Custom exception for testing purposes that mimics Ktor's ClientRequestException structure
class KtorTestException(
    val statusCode: Int,
    val errorBody: String,
    val identifier: String
) : Exception("HTTP $statusCode: $errorBody")