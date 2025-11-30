package az.pashabank.data.di

import az.pashabank.starter.data.remote.error.RemoteErrorMapper
import az.pashabank.data.local.settings.AppPreferences
import az.pashabank.data.local.settings.AppSettings
import az.pashabank.data.local.settings.AppSettingsDataSourceImpl
import az.pashabank.starter.data.remote.CardApiService
import az.pashabank.starter.data.remote.CustomerApiService
import az.pashabank.starter.data.remote.TransactionApiService
import az.pashabank.data.repository.AuthRepositoryImpl
import az.pashabank.data.repository.CardRepositoryImpl
import az.pashabank.data.repository.CustomerRepositoryImpl
import az.pashabank.data.repository.ErrorConverterRepositoryImpl
import az.pashabank.data.repository.TransactionRepositoryImpl
import az.pashabank.starter.data.local.card.CardLocalDataSource
import az.pashabank.starter.data.local.customer.CustomerLocalDataSource
import az.pashabank.starter.data.local.transaction.TransactionLocalDataSource
import az.pashabank.starter.domain.di.ERROR_MAPPER_NETWORK
import az.pashabank.starter.domain.di.IO_CONTEXT
import az.pashabank.starter.domain.exceptions.ErrorMapper
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import az.pashabank.starter.domain.repository.AuthRepository
import az.pashabank.starter.domain.repository.CardRepository
import az.pashabank.starter.domain.repository.CustomerRepository
import az.pashabank.starter.domain.repository.ErrorConverterRepository
import az.pashabank.starter.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val dataModule = module {

    single<CoroutineContext>(named(IO_CONTEXT)) { Dispatchers.IO }

    //////////////////////////////////// REPOSITORY ////////////////////////////////////

    factory<ErrorConverterRepository> {
        ErrorConverterRepositoryImpl(
            jsonSerializer = get()
        )
    }

    factory<AppSettingsDataSource> {
        AppSettingsDataSourceImpl(
            appSettings = get(),
        )
    }

    factory<CustomerRepository> {
        CustomerRepositoryImpl(
            api = get<CustomerApiService>(),
            customerLocalDataSource = get<CustomerLocalDataSource>()
        )
    }

    factory<CardRepository> {
        CardRepositoryImpl(
            api = get<CardApiService>(),
            customerLocalDataSource = get<CustomerLocalDataSource>(),
            cardLocalDataSource = get<CardLocalDataSource>()
        )
    }

    factory<AuthRepository> {
        AuthRepositoryImpl()
    }

    factory<TransactionRepository> {
        TransactionRepositoryImpl(
            api = get<TransactionApiService>(),
            customerLocalDataSource = get<CustomerLocalDataSource>(),
            transactionLocalDataSource = get<TransactionLocalDataSource>()
        )
    }

    //////////////////////////////////// LOCAL ////////////////////////////////////
    factory<AppSettings> { AppPreferences(context = androidContext()) }

    //////////////////////////////////// ERROR MAPPER ////////////////////////////////////
    factory<ErrorMapper>(named(ERROR_MAPPER_NETWORK)) { RemoteErrorMapper() }
}