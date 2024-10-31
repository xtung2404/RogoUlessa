package utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar

object TimeUtils {
    fun minuteToHourStr(time: Int): String {
        if (time < 0) return ""
        val hour = time / 60
        val min = time % 60
        return "${if (hour < 10) "0$hour" else hour}:${if (min < 10) "0$min" else min}"
    }

//    fun getCurrentDate(milis: Long) : String {
//        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
//        return simpleDateFormat.format(milis)
//    }

    fun getCurrentDate(year: Int, days: Int) : String {
        val date = LocalDate.ofYearDay(year, days)
        return String.format("%02d/%02d", date.dayOfMonth, date.month.value)
    }

    fun getCurrentDate(year: Int, month: Int, day: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.get(Calendar.DAY_OF_YEAR)
    }
}