package az.pashabank.presentation.base

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import az.pashabank.starter.domain.constant.AppLanguage
import az.pashabank.starter.domain.repository.AppSettingsDataSource
import kotlinx.coroutines.launch


class LanguageContextWrapper(private val dataSource: AppSettingsDataSource) {

    /**
     * Observe and apply the saved language to the app.
     * This should be called from Application.onCreate() to set the app-wide locale.
     */
    fun observeAndApplyLanguage() {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            dataSource.observeLanguage().collect { language ->
                val localeList = LocaleListCompat.forLanguageTags(language.name.lowercase())
                AppCompatDelegate.setApplicationLocales(localeList)
            }
        }
    }

    /**
     * Update the language and recreate the current activity.
     * This should be called when the user changes the language.
     */
    suspend fun updateLanguage(activity: Activity, language: AppLanguage) {
        dataSource.setAppLanguage(language)
        // The observeLanguage collector will automatically apply the new language
        // Activity will be recreated automatically by AppCompatDelegate
    }
}
