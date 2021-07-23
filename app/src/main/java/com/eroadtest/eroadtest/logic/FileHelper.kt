package com.eroadtest.eroadtest.logic

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.Constant
import com.eroadtest.eroadtest.Constant.Exception.CREATE_FILE_INTERVAL
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.OutputModel
import com.eroadtest.eroadtest.model.SensorDataModel
import com.google.gson.Gson
import java.io.File
import java.lang.Exception
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class FileHelper constructor(
    private val context: Context = MyApplication.applicationContext()
) {

    private var newList = ArrayList<SensorDataModel>()
    private val writeList = ArrayList<SensorDataModel>()
    private var newTimestamp = 0L
    private var newFileName = "Sensor_create_fail"


    fun handleCreateFileLogic(model: SensorDataModel) {
        if (newTimestamp == 0L) {
            createNextTimestamp(model.t_sec)
        } else {
            if (newTimestamp >= model.t_sec) {
                newList.add(model)
            } else {
                writeList.addAll(newList)
                newList.clear()
                newList.add(model)
                createFileNameByTimestamp(newTimestamp)
                createNextTimestamp(model.t_sec)
                writeFile()
            }
        }
    }


    private fun createNextTimestamp(timestamp: Long) {
        //create a new timestamp to record when the next file will create
        newTimestamp = timestamp + CREATE_FILE_INTERVAL
    }

    private fun createFileNameByTimestamp(timestamp: Long) {
        val datePart = dateFormat(timestamp)
        newFileName = "Sensor_$datePart.sns"
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun writeFile() {
        if (!isExternalStorageWritable())
            throw Exception(Constant.Exception.EXTERNAL_NO_SPACE)

        val myFile = File(context.getExternalFilesDir(null), newFileName)
        val outputModel = OutputModel(
            sensorData = writeList,
            start = writeList.first().t_sec,
            end = writeList.last().t_sec
        )
        myFile.bufferedWriter().use { writer ->
            val string = Gson().toJson(outputModel)
            writer.write(string)
        }
        writeList.clear()
    }

    private fun dateFormat(timestampL: Long): String {
        val stamp = Timestamp(timestampL)
        val date = Date(stamp.time).toInstant()
        return DateTimeFormatter
            .ofPattern("yyyy-MM-dd-hh-mm-ss-SSS")
            .withZone(ZoneId.of("UTC"))
            .format(date)
    }
}