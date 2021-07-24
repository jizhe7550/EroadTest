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
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import java.lang.Exception

@RequiresApi(Build.VERSION_CODES.O)
class SearchDataHelper constructor(
    private val context: Context = MyApplication.applicationContext(),
    private val dateUtil: DateUtil = DateUtil(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val recordManager: RecordManager = RecordManager(),
    private val fileHelper: FileHelper = FileHelper(),
) {

    /**
     *
     */
    fun searchDataByTime(start: Long, end: Long) {
        CoroutineScope(dispatcher).launch {
            var sensorDataModels = ArrayList<SensorDataModel>()
            val files = fileHelper.listRecordFiles()
            files?.forEach { file ->
                // get date part in filename
                val dateStr = file.name.substring(7, file.name.length - 4)
                val timestamp = dateUtil.dateStrToTimestamp(dateStr)
                if (timestamp in start - SensorDataManager.CREATE_FILE_INTERVAL..end) {
                    val outputModel = CoroutineScope(dispatcher).async {
                        fileHelper.readFile(file.name)
                    }
                    val sensorData = outputModel.await().sensorData
                    sensorDataModels.addAll(sensorData)
                }
            }
        }
    }
}