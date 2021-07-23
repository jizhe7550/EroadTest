package com.eroadtest.eroadtest.component

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.R
import com.eroadtest.eroadtest.logic.SearchDataHelper

class MainActivity : AppCompatActivity() {
    private lateinit var sensorServiceIntent: Intent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startSensorService()
        //SearchDataHelper().searchDataByTime(1627043804641,1627043884924)
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