package az.pashabank.data.repository

import az.pashabank.starter.data.local.card.CardLocalDataSource
import az.pashabank.starter.data.local.customer.CustomerLocalDataSource
import az.pashabank.data.mapper.toDomain
import az.pashabank.data.mapper.toLocal
import az.pashabank.data.remote.CardApiService
import az.pashabank.starter.domain.model.customer.Card
import az.pashabank.starter.domain.repository.CardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardRepositoryImpl : CardRepository {

    private val api: CardApiService
    private val cardLocalDataSource: CardLocalDataSource
    private val customerLocalDataSource: CustomerLocalDataSource

    constructor(api: CardApiService, cardLocalDataSource: CardLocalDataSource, customerLocalDataSource: CustomerLocalDataSource) {
        this.api = api
        this.cardLocalDataSource = cardLocalDataSource
        this.customerLocalDataSource = customerLocalDataSource
    }

    override fun observeCards(): Flow<List<Card>> {
        val customerId = customerLocalDataSource.getCustomerId()
        return cardLocalDataSource.observeCards(customerId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncCards() {
        val customerId = customerLocalDataSource.getCustomerId()
        val remoteCards = api.getCards(customerId)
        val localCards = remoteCards.map { it.toLocal() }
        cardLocalDataSource.insertCards(customerId, localCards)
    }

}