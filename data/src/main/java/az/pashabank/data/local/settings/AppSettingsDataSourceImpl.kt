package az.pashabank.data.local.settings

import az.pashabank.starter.domain.constant.AppLanguage
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import kotlinx.coroutines.flow.Flow

class AppSettingsDataSourceImpl(
    private val appSettings: AppSettings
) : AppSettingsDataSource {

    override fun getAppLanguage(): AppLanguage = appSettings.getAppLanguage()

    override fun setAppLanguage(langCode: AppLanguage) {
        appSettings.setAppLanguage(langCode)
    }

    override fun observeLanguage(): Flow<AppLanguage> = appSettings.observeLanguage()
}