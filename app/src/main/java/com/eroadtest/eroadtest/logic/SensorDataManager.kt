package com.eroadtest.eroadtest.logic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.SensorDataModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class SensorDataManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): SensorEventListener {

    private var applicationContext: Context = MyApplication.applicationContext()
    private val channel = Channel<SensorDataModel>(Channel.UNLIMITED)
    private val job = Job()

    init {
        setUpSensorStuff()
        receiveModelFromChannel()
    }

    private fun setUpSensorStuff() {
        // Create the sensor manager
        val sensorManager =
            applicationContext.getSystemService(LifecycleService.SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
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
                // TODO handleCreateFileLogic(model)
            }
        }
    }

    fun cleanRes() {
        job?.cancel()
        channel?.close()
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