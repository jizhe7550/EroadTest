package com.eroadtest.eroadtest.model

data class OutputModel(
    val sensorData:List<SensorDataModel>,
    val start:Long,
    val end:Long
)
