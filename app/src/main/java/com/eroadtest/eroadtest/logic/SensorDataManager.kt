package com.eroadtest.eroadtest.logic

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.component.TAG
import com.eroadtest.eroadtest.model.SensorDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class SensorDataManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val recordManager: RecordManager = RecordManager()
) : SensorEventListener {

    private val channel = Channel<SensorDataModel>(Channel.UNLIMITED)
    private val job = Job()

    /**
     * this is a timestamp for notice fileHelper to create a record file first time next time.
     * normally it is current timestamp plus 3 mins,
     * but when App starts (init 0), it will be the first listened return model timestamp plus + 3 mins
     */
    private var noticeWriteFileTimestamp = 0L

    /**
     * a list for collect models from channel in order to pass fileHelper to write to file
     */
    private var currentList = ArrayList<SensorDataModel>()

    init {
        receiveModelFromChannel()
    }

    private fun sendModelToChannel(model: SensorDataModel) {
        if (channel.isClosedForSend)
            return
        Log.i(TAG, "offer${model}")
        channel.offer(model)
    }

    private fun receiveModelFromChannel() {
        CoroutineScope(dispatcher + job).launch {
            for (model in channel) {
                handleIntervalList(model)
            }
        }
    }

    private fun handleIntervalList(model: SensorDataModel) {
        val modelTimestamp = model.t_sec
        if (noticeWriteFileTimestamp == 0L) {
            getFileCreateTime(modelTimestamp)
        } else {
            /* if noticeWriteFileTimestamp is greater than a model's return time,
             * add model to list.
             * Otherwise,that means it is time to notice fileHelper to write and create next notice timestamp,
             * clear the list for next interval, but don't forget to add the model that comes in this time.
             */
            if (noticeWriteFileTimestamp >= modelTimestamp) {
                currentList.add(model)
            } else {
                recordManager.addWriteList(currentList)
                currentList.clear()
                currentList.add(model)
                getFileCreateTime(noticeWriteFileTimestamp)
                recordManager.apply {
                    recordDataToFile(noticeWriteFileTimestamp, createOutputModel())
                }
            }
        }
    }

    /**
     * create the first timestamp to notice fileHelper to do it's task (create file)
     */
    private inline fun getFileCreateTime(timestamp: Long) {
        noticeWriteFileTimestamp = timestamp + CREATE_FILE_INTERVAL
    }

    fun cleanRes() {
        job.cancel()
        channel.close()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // create a model for every callback
            val sensorDataModel = event.toSensorDataModel()
            // send it to channel, make sure it is in sequence to handle, even suspend.

            Log.i(TAG, "callBack${sensorDataModel}")
            sendModelToChannel(sensorDataModel)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    companion object {
        const val CREATE_FILE_INTERVAL = 3 * 60 * 1000L
    }
}