package com.eroadtest.eroadtest

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.eroadtest.eroadtest.logic.SensorDataManager

class SensorService : LifecycleService() {
    private lateinit var sensorDataManager: SensorDataManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        initSensorDataManager()
        return START_STICKY
    }

    private fun initSensorDataManager() {
        SensorDataManager()
    }

    override fun onDestroy() {
        sensorDataManager.cleanRes()
        super.onDestroy()
    }
}