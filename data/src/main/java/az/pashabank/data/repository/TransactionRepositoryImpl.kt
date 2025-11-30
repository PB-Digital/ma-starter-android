package az.pashabank.data.repository

import az.pashabank.starter.data.local.customer.CustomerLocalDataSource
import az.pashabank.starter.data.local.transaction.TransactionLocalDataSource
import az.pashabank.data.mapper.toDomain
import az.pashabank.data.mapper.toLocal
import az.pashabank.starter.data.remote.TransactionApiService
import az.pashabank.starter.domain.model.customer.Transaction
import az.pashabank.starter.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val api: TransactionApiService,
    private val transactionLocalDataSource: TransactionLocalDataSource,
    private val customerLocalDataSource: CustomerLocalDataSource
) : TransactionRepository {

    override fun observeTransactions(cardId: String): Flow<List<Transaction>> {
        return transactionLocalDataSource.observeTransactions(cardId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncTransactions(cardId: String) {
        val remoteTransactions =
            api.getTransactions(customerLocalDataSource.getCustomerId(), cardId)
        val localTransactions = remoteTransactions.map { it.toLocal() }
        transactionLocalDataSource.insertTransactions(cardId, localTransactions)
    }

}