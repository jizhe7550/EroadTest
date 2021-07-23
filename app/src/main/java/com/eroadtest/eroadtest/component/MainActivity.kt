package com.eroadtest.eroadtest.component

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eroadtest.eroadtest.R

class MainActivity : AppCompatActivity() {
    private lateinit var sensorServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startSensorService()
    }

    private fun startSensorService() {
        sensorServiceIntent = Intent(this, SensorService::class.java)
        startService(sensorServiceIntent)
    }

    override fun onDestroy() {
        stopService(sensorServiceIntent)
        super.onDestroy()
    }


}