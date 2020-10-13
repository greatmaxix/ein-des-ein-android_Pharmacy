package com.pharmacy.core.extensions

import org.apache.commons.lang3.time.DateUtils
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.Calendar.DATE
import java.util.Calendar.MONTH

fun Calendar.isCalendarSame(second: Calendar) =
    this[Calendar.YEAR] == second[Calendar.YEAR] && this[MONTH] == second[MONTH] && this[DATE] == second[DATE]

val Calendar.toCalendarRange
    get() = time.toCalendar.rangeTo(DateUtils.addMilliseconds(DateUtils.ceiling(time, DATE), -1).toCalendar)

val Calendar.toCalendarStart: Calendar
    get() = DateUtils.truncate(time, DATE).toCalendar

val Calendar.toCalendarEnd: Calendar
    get() = DateUtils.addMilliseconds(DateUtils.ceiling(time, DATE), -1).toCalendar

val Calendar.toMMMDD: String
    get() = SimpleDateFormat("MMM dd", Locale.US).format(Date().apply {
        time = timeInMillis
    })

val Calendar.toMMMMDD: String
    get() = SimpleDateFormat("MMMM dd", Locale.US).format(Date().apply {
        time = timeInMillis
    })



val Calendar.toDate
    get() = Date().apply { time = timeInMillis }

val Calendar.lastDay
    get() = getActualMaximum(Calendar.DAY_OF_MONTH)

val Calendar.firstDay
    get() = getActualMinimum(Calendar.DAY_OF_MONTH)

fun Calendar.toCalendarRange(calendar: Calendar) = mutableListOf<Calendar>().apply {
    while (before(calendar)) {
        this.add(toCalendarEnd)
        add(MONTH, 1)
    }
}

fun Calendar.toDateRange(calendar: Calendar) = mutableListOf<Date>().apply {
    while (before(calendar)) {
        this.add(toCalendarEnd.toDate)
        add(MONTH, 1)
    }
}

fun Calendar.isEqualYYYYMMDD(calendar: Calendar?) =
    calendar?.let {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdf.format(time) == sdf.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    } ?: false


val Date.toTitle: String
    get() = SimpleDateFormat("MMMM yyyy", Locale.US).format(this)

val Date.toCalendar: Calendar get() = Calendar.getInstance().apply { time = this@toCalendar }

val LocalDate.toDate: Date get() = Date.from(atStartOfDay(ZoneId.systemDefault()).toInstant())

val Calendar.toLocalDate: LocalDate get() = time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()