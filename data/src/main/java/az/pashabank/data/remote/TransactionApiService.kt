package az.pashabank.data.remote

import az.pashabank.data.remote.model.transaction.TransactionRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TransactionApiService(private val httpClient: HttpClient) {
    suspend fun getTransactions(customerId: String, cardId: String): List<TransactionRemoteDto> {
        return httpClient.get("pb/v1/customers/$customerId/cards/$cardId/transactions").body()
    }
}
