package az.pashabank.data.mapper

import az.pashabank.starter.data.extensions.getDate
import az.pashabank.starter.data.local.card.model.CardLocalDto
import az.pashabank.starter.data.local.customer.model.CustomerLocalDto
import az.pashabank.starter.data.local.transaction.model.TransactionLocalDto
import az.pashabank.starter.domain.model.customer.*
import az.pashabank.starter.domain.model.customer.Card
import az.pashabank.starter.domain.model.customer.Currency
import az.pashabank.starter.domain.model.customer.Customer

fun CustomerLocalDto.toDomain() = Customer(
    id = id,
    name = name,
    phone = phone,
    createdAt = createdAt
)

fun CardLocalDto.toDomain() = Card(
    id = id,
    customerId = customerId,
    currency = Currency.valueOf(currency),
    status = ECardStatus.getCardStatus(status),
    type = ECardType.getCardType(type),
    balance = balance.toDoubleOrNull() ?: 0.0,
    pan = pan,
    createdAt = createdAt
)

fun TransactionLocalDto.toDomain() = Transaction(
    id = id,
    cardId = cardId,
    category = ETransactionCategory.getCategory(category),
    title = title,
    createdAt = createdAt.getDate() ,
    amount = amount,
    currency = currency
)
