package com.eroadtest.eroadtest.logic

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.OutputModel
import com.eroadtest.eroadtest.model.SensorDataModel
import com.eroadtest.eroadtest.util.DateUtil
import com.eroadtest.eroadtest.util.FileHelper
import com.google.gson.Gson
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class RecordManager constructor(
    private val dateUtil: DateUtil = DateUtil(),
    private val fileUtil: FileHelper = FileHelper()
) {
    private val writeList = ArrayList<SensorDataModel>()

    fun createOutputModel(): OutputModel {
        return OutputModel(writeList)
    }

    fun recordDataToFile(timestampL: Long, outputModel: OutputModel) {
        val newFileName = createFileNameByTimestamp(timestampL)
        val outputModelJson = Gson().toJson(outputModel)
        fileUtil.writeFile(newFileName, outputModelJson)
        writeList.clear()
    }

    fun addWriteList(list: ArrayList<SensorDataModel>) {
        writeList.addAll(list)
    }

    private fun createFileNameByTimestamp(timestamp: Long): String {
        val datePart = dateUtil.dateFormat(timestamp)
        return "Sensor_$datePart.sns"
    }
}