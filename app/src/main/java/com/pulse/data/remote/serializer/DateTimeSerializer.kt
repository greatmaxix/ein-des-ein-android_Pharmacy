package com.pulse.data.remote.serializer

import android.text.TextUtils
import com.google.gson.*
import timber.log.Timber
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeSerializer : JsonDeserializer<LocalDateTime>,
    JsonSerializer<LocalDateTime> {

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): LocalDateTime? {

        val date = json.asJsonPrimitive.asString
        if (TextUtils.isEmpty(date)) {
            return null
        }

        if (TextUtils.isDigitsOnly(date)) {
            Timber.d("Digits only date $date")
            val dateLong = date.toLong()
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(dateLong), ZoneId.systemDefault())
        }

        val patternList = listOf(
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ISO_OFFSET_DATE,
            DateTimeFormatter.ISO_DATE,
            DateTimeFormatter.ISO_LOCAL_TIME,
            DateTimeFormatter.ISO_OFFSET_TIME,
            DateTimeFormatter.ISO_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_OFFSET_DATE_TIME,
            DateTimeFormatter.ISO_ZONED_DATE_TIME,
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ISO_ORDINAL_DATE,
            DateTimeFormatter.ISO_WEEK_DATE,
            DateTimeFormatter.ISO_INSTANT,
            DateTimeFormatter.BASIC_ISO_DATE
        )

        patternList.forEach {
            try {
                return LocalDateTime.parse(date, it)
            } catch (e: Exception) {
                // ignore and try next
            }
        }

        Timber.e("Date $date can`t be parsed properly")
        return null
    }

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src.toCalendar().time.time / 1000) // all date in app in seconds
    }
}

fun LocalDateTime.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month.value - 1)
    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    cal.set(Calendar.HOUR_OF_DAY, hour)
    cal.set(Calendar.MINUTE, minute)
    cal.set(Calendar.SECOND, second)
    return cal
}
