package com.eroadtest.eroadtest.component

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import com.eroadtest.eroadtest.R
import com.eroadtest.eroadtest.util.FileHelper

const val TAG = "MySensor"

class MainActivity : AppCompatActivity() {
    private lateinit var sensorServiceIntent: Intent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val tbService = findViewById<ToggleButton>(R.id.tb)
        val btnCheckFiles = findViewById<Button>(R.id.btnCheckFiles)
        val tvFiles = findViewById<TextView>(R.id.tvFiles)
        tbService.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                startSensorService()
            }else{
                stopSensorService()
            }
        }
        btnCheckFiles.setOnClickListener {
            tvFiles.text = FileHelper().listRecordFileNames().toString()
        }
    }

    private fun startSensorService() {
        sensorServiceIntent = Intent(this, SensorService::class.java)
        startService(sensorServiceIntent)
        Log.i(TAG,"startService")
    }

    private fun stopSensorService(){
        if (::sensorServiceIntent.isInitialized){
            stopService(sensorServiceIntent)
        }
        Log.i("TAG","stopService")
    }

    override fun onDestroy() {
        stopSensorService()
        super.onDestroy()
    }


}