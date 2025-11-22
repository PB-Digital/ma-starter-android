@file:OptIn(FormatStringsInDatetimeFormats::class, ExperimentalTime::class)

package az.pashabank.data.extensions

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun String.getDate(): LocalDateTime {
    return try {
        Instant.parse(this).toLocalDateTime(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
        Timber.e(e, "Can not parse date string: $this, using current time")
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
}