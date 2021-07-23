package com.eroadtest.eroadtest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DateUtil{
    private val formatter = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.getDefault())

    fun dateFormat(timestampL: Long): String {
        val date = Date(timestampL)
        return formatter.format(date)
    }

    fun dateStrToTimestamp(dateStr: String): Long {
        val parsedDate = formatter.parse(dateStr)
        val timestamp = Timestamp(parsedDate.time)
        return timestamp.time

    }

}