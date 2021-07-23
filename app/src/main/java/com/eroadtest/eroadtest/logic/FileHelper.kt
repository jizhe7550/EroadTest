package com.eroadtest.eroadtest.logic

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.OutputModel
import com.eroadtest.eroadtest.model.SensorDataModel
import com.eroadtest.eroadtest.util.DateUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.Exception
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class FileHelper constructor(
    private val context: Context = MyApplication.applicationContext(),
    private val dateUtil: DateUtil = DateUtil()
) {
    private val writeList = ArrayList<SensorDataModel>()

    fun createOutputModel():OutputModel{
        return OutputModel(writeList)
    }
    fun writeFile(timestampL: Long,outputModel:OutputModel) {
        if (!isExternalStorageWritable())
            throw Exception(EXTERNAL_NO_SPACE)

        val newFileName = createFileNameByTimestamp(timestampL)
        val myFile = File(context.getExternalFilesDir(null), newFileName)

        try {
            myFile.bufferedWriter().use { writer ->
                val string = Gson().toJson(outputModel)
                writer.write(string)
            }
        } catch (e: Exception) {
            throw Exception("file $newFileName wrote fail!")
        } finally {
            writeList.clear()
        }
    }

    fun addWriteList(list: ArrayList<SensorDataModel>) {
        writeList.addAll(list)
    }

    private fun createFileNameByTimestamp(timestamp: Long): String {
        val datePart = dateUtil.dateFormat(timestamp)
        return "Sensor_$datePart.sns"
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun readFile(filename: String): OutputModel {
        var outputModel: OutputModel
        try {
            val myFile = File(context.getExternalFilesDir(null), filename)
            myFile.bufferedReader().use { reader ->
                val outputModelType = object : TypeToken<OutputModel>() {}.type
                outputModel = Gson().fromJson(reader, outputModelType)
            }
            return outputModel
        } catch (e: Exception) {
            throw Exception("file $filename read fail!")
        }
    }

    companion object {
        const val EXTERNAL_NO_SPACE = "Cannot record data"
    }
}