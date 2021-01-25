package dev.arpan.imc.demo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun getFormattedDate(timeInMillis: Long): String? {
        val date = Date(timeInMillis)
        return SimpleDateFormat("MM/dd/yyyy '-' hh:mm a", Locale.ENGLISH).format(date)
    }
}
