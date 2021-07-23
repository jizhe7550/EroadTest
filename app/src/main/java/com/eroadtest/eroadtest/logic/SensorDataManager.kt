package com.eroadtest.eroadtest.logic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.model.SensorDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

@RequiresApi(Build.VERSION_CODES.O)
class SensorDataManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val fileHelper: FileHelper = FileHelper()
): SensorEventListener {

    private val channel = Channel<SensorDataModel>(Channel.UNLIMITED)
    private val job = Job()

    init {
        receiveModelFromChannel()
    }

    private fun sendModelToChannel(model: SensorDataModel) {
        CoroutineScope(dispatcher+job).launch {
            Log.i("Sensor", "send${model}")
            channel.send(model)
        }
    }

    private fun receiveModelFromChannel() {
        CoroutineScope(dispatcher+job).launch {
            for (model in channel) {
                 fileHelper.handleCreateFileLogic(model)
            }
        }
    }

    private fun handleIntervalList(){
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

    fun cleanRes() {
        job.cancel()
        channel.close()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // create a model for every callback
            val sensorDataModel = SensorDataModel(x_acc = x, y_acc = y, z_acc = z)

            // send it to channel, make sure it is in sequence to handle, even suspend.
            sendModelToChannel(sensorDataModel)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}