package com.eroadtest.eroadtest.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateFormat(timestampL: Long): String {
        val stamp = Timestamp(timestampL)
        val date = Date(stamp.time).toInstant()
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd-hh-mm-ss-SSS")
            .withZone(ZoneId.of("UTC"))
            .format(date)
    }

}