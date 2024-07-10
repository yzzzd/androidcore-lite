package com.nuvyz.core.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateFormatter {

    private val localeDefault: Locale by lazy { Locale.getDefault() }

    //val localeEN = Locale.US
    //val localeID = Locale("id", "ID")

    fun pretty(raw: String, locale: Locale = localeDefault): String {
        val formatter1 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", locale)
        val date = formatter1.parse(raw)
        val formatter2 = SimpleDateFormat("d MMM yyyy, HH:mm", locale)
        return formatter2.format(date)
    }

    fun pretty(raw: String, from: String, to: String, locale: Locale = localeDefault): String {
        val formatter1 = SimpleDateFormat(from, locale)
        val date = formatter1.parse(raw)
        val formatter2 = SimpleDateFormat(to, locale)
        return formatter2.format(date)
    }

    fun pretty(raw: Calendar, format: String, locale: Locale = localeDefault): String {
        val formatter2 = SimpleDateFormat(format, locale)
        return formatter2.format(raw.time)
    }

    fun timeNow(locale: Locale = localeDefault): String {
        val formatter = SimpleDateFormat("HH:mm", locale)
        val date = Calendar.getInstance().time
        return formatter.format(date)
    }

    fun dateNow(format: String = "yyyy-MM-dd", locale: Locale = localeDefault): String {
        val formatter = SimpleDateFormat(format, locale)
        val date = Calendar.getInstance().time
        return formatter.format(date)
    }

    fun addZero(value: Int): String {
        return if (value < 10) {
            "0$value"
        } else {
            "$value"
        }
    }
}