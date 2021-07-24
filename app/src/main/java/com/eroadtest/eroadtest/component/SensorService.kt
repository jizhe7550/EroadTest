package com.eroadtest.eroadtest.component

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.logic.SensorDataManager

@RequiresApi(Build.VERSION_CODES.O)
class SensorService : Service() {

    private var sensorDataManager = SensorDataManager()
    private lateinit var sensorManager:SensorManager
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        setUpSensorStuff()
        return START_STICKY
    }


    private fun setUpSensorStuff() {
        // Create the sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                sensorDataManager,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(sensorDataManager)
        sensorDataManager.cleanRes()
        Log.i(TAG,"service destroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}