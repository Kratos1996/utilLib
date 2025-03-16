package com.techhub.util.core

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateUtil {
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_DD_MM_YYYY = "dd-MM-yyyy"
    const val FORMAT_MM_DD_YYYY = "MM-dd-yyyy"
    const val FORMAT_FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_HH_MM_SS = "HH:mm:ss"
    private var millisInOneDay = 86400000L
    const val HH_MM = "HH:mm"
    const val HH_MM_SS = "HH:mm:ss"
    const val MMM_YY = "MMM yy"
    const val MMM_DD_YYYY = "MMM dd, yyyy"
    const val DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss"
    const val DD_MM_YY_SLASH = "dd/MM/yy"
    const val YYYY_MM_DD_HYPHEN = "yyyy-MM-dd"
    const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val dateFormatddMMyyHHmmss = "dd/MM/yy HH:mm:ss"
    const val DATE_FORMATE_HISTORY = "dd/MM/yy HH:mm:ss"
    const val DATE_FORMATE_WEB = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_FORMATE_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMATE_DD_MM_YYYY = "dd/MM/yyyy"
    const val DATE_FORMAT_DD_MM_YY = "dd/MM/yy"
    const val DATE_FORMAT_MMM_YY = "MMM yyyy"
    const val DATE_FORMATE_YEAR_FRONT = "yyyy-MM-dd"
    private const val DATE_FORMATE_MONTH_CAP= "MMM dd,yyyy"

    fun getCurrentDate(format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date())
    }

    fun convertDateFormat(dateString: String, fromFormat: String, toFormat: String): String {
        return try {
            val sdfFrom = SimpleDateFormat(fromFormat, Locale.getDefault())
            val sdfTo = SimpleDateFormat(toFormat, Locale.getDefault())
            val date = sdfFrom.parse(dateString)
            date?.let { sdfTo.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    fun getTimeDifferenceInMills(startDate: String, endDate: String, format: String): Long {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            if (start != null && end != null) {
                end.time - start.time
            } else {
                0L
            }
        } catch (e: Exception) {
            0L
        }
    }
    fun convertTimeMillisToDate(timeMillis: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date(timeMillis))
    }

    fun convertDateToTimeMillis(dateString: String, format: String): Long {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val date = sdf.parse(dateString)
            date?.time ?: 0L
        } catch (e: Exception) {
            0L
        }
    }
    fun convertDateToString(dateString: String, currentFormat: String, newFormat: String, showDate:Boolean = true): String {
        val finalDate = convertDateFormat(dateString = dateString, fromFormat = currentFormat, toFormat = newFormat)
        return try {
            val sdfFrom = SimpleDateFormat(currentFormat, Locale.getDefault())
            val date = sdfFrom.parse(dateString)
            val calender =  Calendar.getInstance()
            val today = calender
            val yesterday = calender.apply { add(Calendar.DAY_OF_YEAR, -1) }
            val inputDate = calender.apply { time = date }
            when {
                inputDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) && inputDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Today"
                inputDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && inputDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> "Yesterday"
                else -> if(showDate) finalDate else getRelativeTimeAgo(dateString = finalDate, format = newFormat)
            }
        } catch (e: Exception) {
            if(showDate) finalDate else getRelativeTimeAgo(dateString = finalDate, format = newFormat)
        }
    }
    fun getWeekdayName(dateString: String, format: String): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val date = sdf.parse(dateString)
            date?.let {
                SimpleDateFormat("EEEE", Locale.getDefault()).format(it)
            } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
    fun getRelativeTimeAgo(dateString: String, format: String): String {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            val date = sdf.parse(dateString) ?: return "Unknown"
            val now = Calendar.getInstance()
            val past = Calendar.getInstance().apply { time = date }
            val diff = now.timeInMillis - past.timeInMillis

            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24
            val weeks = days / 7
            val months = days / 30
            val years = days / 365

            when {
                days in 1..6 -> "$days day${if (days > 1) "s" else ""} ago"
                weeks in 1..4 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
                months in 1..11 -> "$months month${if (months > 1) "s" else ""} ago"
                years in 1..99 -> "$years year${if (years > 1) "s" else ""} ago"
                else -> "More than a century ago"
            }
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun countTimeLeftVerbose(dateString: String, format: String): String {
        val targetTimeMillis = convertDateToTimeMillis(dateString, format) ?: return "Invalid Date"

        val currentTimeMillis = System.currentTimeMillis()
        if (targetTimeMillis <= currentTimeMillis) return "Time Expired"

        val timeDifferenceMillis = targetTimeMillis - currentTimeMillis

        val timeDifferenceSeconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis)

        val days = TimeUnit.SECONDS.toDays(timeDifferenceSeconds)
        val daysInSeconds = TimeUnit.DAYS.toSeconds(days)

        val remainingSecondsAfterDays = timeDifferenceSeconds - daysInSeconds
        val hours = TimeUnit.SECONDS.toHours(remainingSecondsAfterDays)

        val hoursInSeconds = TimeUnit.HOURS.toSeconds(hours)
        val remainingSecondsAfterHours = remainingSecondsAfterDays - hoursInSeconds

        val minutes = TimeUnit.SECONDS.toMinutes(remainingSecondsAfterHours)

        return when {
            days > 0 -> "$days Day(s)"
            hours > 0 -> "$hours Hour(s)"
            minutes > 0 -> "$minutes Minute(s)"
            else -> "Less than a minute"
        }
    }



    fun isExpired(dateString: String, format: String) : Boolean {
        try {
            val simpleDateFormat = SimpleDateFormat(format,Locale.getDefault())
            val dateFormatted = simpleDateFormat.parse(dateString)
            val dateNow = Date(System.currentTimeMillis())
            if(dateNow == dateFormatted || dateNow.after(dateFormatted)) {
                return true
            }
            return false
        }
        catch (ignore: Exception){}
        return false
    }

    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(selectedDate)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
    fun showTimePicker(context: Context,is24HourView:Boolean = true, onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(context, { _, hourOfDay, minute ->
            val selectedTime = "$hourOfDay:$minute"
            onTimeSelected(selectedTime)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HourView).show()
    }

    fun showDateTimePicker(context: Context,is24HourView:Boolean = true, onDateTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            TimePickerDialog(context, { _, hourOfDay, minute ->
                val selectedDateTime = "$selectedDate $hourOfDay:$minute"
                onDateTimeSelected(selectedDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), is24HourView).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}