package com.eroadtest.eroadtest.logic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LifecycleService
import com.eroadtest.eroadtest.MyApplication
import com.eroadtest.eroadtest.model.SensorDataModel

class SensorDataManager: SensorEventListener {

    private var applicationContext: Context = MyApplication.applicationContext()

    init {
        setUpSensorStuff()
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

    fun cleanRes(){

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // create a model for every callback
            val sensorDataModel = SensorDataModel(x_acc = x, y_acc = y, z_acc = z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}