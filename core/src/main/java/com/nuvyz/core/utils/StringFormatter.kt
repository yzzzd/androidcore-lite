package com.nuvyz.core.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object StringFormatter {

    private val currentLocale = Locale("id", "ID")

    private val wholeNumberDecimalFormat = (NumberFormat.getNumberInstance(currentLocale) as DecimalFormat).apply {
        applyPattern("#,##0")
    }

    fun addRp(value: Long?): String {
        return try {
            //"Rp${String.format(currentLocale, "%,d", value)}"
            "Rp${wholeNumberDecimalFormat.format(value)}"
        } catch (e: Exception) {
            "Rp"
        }
    }

    fun addRp(value: Double?): String {
        return try {
            //"Rp${String.format(currentLocale, "%,d", value)}"
            "Rp${wholeNumberDecimalFormat.format(value)}"
        } catch (e: Exception) {
            "Rp"
        }
    }

    fun noRp(value: Long?): String {
        return try {
            //"Rp${String.format(currentLocale, "%,d", value)}"
            wholeNumberDecimalFormat.format(value)
        } catch (e: Exception) {
            "0"
        }
    }

    fun geolocation(value: Double?): String {
        return try {
            String.format(currentLocale, "%.3f", value)
        } catch (e: Exception) {
            "-"
        }
    }

    fun zeroDecimal(value: Double?): String {
        return try {
            DecimalFormat("0.#").format(value)
        } catch (e: Exception) {
            "-"
        }
    }
}