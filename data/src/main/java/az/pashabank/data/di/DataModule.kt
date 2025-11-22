package az.pashabank.data.di

import az.pashabank.data.errors.RemoteErrorMapper
import az.pashabank.data.local.settings.AppPreferences
import az.pashabank.data.local.settings.AppSettings
import az.pashabank.data.local.settings.AppSettingsDataSourceImpl
import az.pashabank.data.remote.CardApiService
import az.pashabank.data.remote.CustomerApiService
import az.pashabank.data.remote.TransactionApiService
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
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

val dataModule = module {

    single<CoroutineContext>(named(IO_CONTEXT)) { Dispatchers.IO }

    //////////////////////////////////// NETWORK ////////////////////////////////////

    single {
        Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            // Install Content Negotiation for JSON
            install(ContentNegotiation) {
                json(get<Json>())
            }

            // Install Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.tag("Ktor").d(message)
                    }
                }
                level = if (getProperty<String>("isDebug") == true.toString()) {
                    LogLevel.ALL
                } else {
                    LogLevel.NONE
                }
            }

            // Default request configuration
            defaultRequest {
                url(getProperty<String>("host"))
            }
        }
    }

    factory<CustomerApiService> { CustomerApiService(get()) }
    factory<CardApiService> { CardApiService(get()) }
    factory<TransactionApiService> { TransactionApiService(get()) }


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