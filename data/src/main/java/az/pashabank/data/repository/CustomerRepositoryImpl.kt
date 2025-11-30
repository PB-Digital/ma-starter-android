package az.pashabank.data.repository

import az.pashabank.starter.data.local.customer.CustomerLocalDataSource
import az.pashabank.data.mapper.toDomain
import az.pashabank.data.mapper.toLocal
import az.pashabank.starter.data.remote.CustomerApiService
import az.pashabank.starter.domain.model.customer.Customer
import az.pashabank.starter.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomerRepositoryImpl(
    private val api: CustomerApiService,
    private val customerLocalDataSource: CustomerLocalDataSource
) : CustomerRepository {

    override fun observeCustomers(): Flow<List<Customer>> {
        return customerLocalDataSource.observeCustomers()
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun syncCustomers() {
        val remoteCustomers = api.getCustomer(customerLocalDataSource.getCustomerId())
        val localCustomers = remoteCustomers.toLocal()
        customerLocalDataSource.insertCustomers(listOf(localCustomers))
    }
}