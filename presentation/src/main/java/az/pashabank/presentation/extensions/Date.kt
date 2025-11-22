@file:OptIn(ExperimentalTime::class, FormatStringsInDatetimeFormats::class)

package az.pashabank.presentation.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import java.util.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun LocalDateTime.getFormattedText(langCode: String = Locale.ENGLISH.toLanguageTag()): String {

    val inputYear = this.year
    val currentYear = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year

    val formatPattern = if (inputYear == currentYear) "dd MM, HH:mm" else "dd MM yyyy"
    val format = LocalDateTime.Format { byUnicodePattern(formatPattern) }

    return this.format(format).capitalizeFirstLetter(Locale(langCode))
}