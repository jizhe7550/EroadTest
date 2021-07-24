package com.eroadtest.eroadtest.logic

import android.os.Build
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.model.modelFile
import com.eroadtest.eroadtest.model.SensorDataModel
import com.eroadtest.eroadtest.util.DateUtil
import com.eroadtest.eroadtest.util.FileHelper
import com.google.gson.Gson
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.O)
class SearchDataHelper constructor(
    private val dateUtil: DateUtil = DateUtil(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val fileHelper: FileHelper = FileHelper(),
) {
    /**
     * Implement a method which takes start and end times as parameter
     * and creates a JSON file in the above format, with all the accelerometer samples
     * recorded in the given interval.
     *
     */
    fun searchDataByTime(start: Long, end: Long) {
        CoroutineScope(dispatcher).launch {
            var sensorDataModels = ArrayList<SensorDataModel>()
            val files = fileHelper.listRecordFiles()
            files?.forEach { file ->
                // get date part in filename
                val dateStr = fileHelper.getDateStrFromSnsFileName(file.name)
                val timestamp = dateUtil.dateStrToTimestamp(dateStr)
                if (timestamp in start - SensorDataManager.CREATE_FILE_INTERVAL..end) {
                    val outputModel = CoroutineScope(dispatcher).async {
                        fileHelper.readFile(file.name)
                    }
                    val sensorData = outputModel.await().sensorData
                    sensorDataModels.addAll(sensorData)
                }
            }

            // TODO: handle edge values in the earliest file and the latest file with filter files [sensorDataModels]

            val outputModel = modelFile(sensorDataModels)
            val outputFilename = createSearchJsonFileName()
            val outputModelJson = Gson().toJson(outputModel)
            fileHelper.writeFile(outputFilename,outputModelJson)
        }
    }

    private fun createSearchJsonFileName(): String {
        val datePart = dateUtil.dateFormat(System.currentTimeMillis())
        return "Sensor_$datePart.json"
    }
}