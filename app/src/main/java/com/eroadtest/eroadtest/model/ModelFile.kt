package com.eroadtest.eroadtest.model

data class OutputModel(
    var sensorData: List<SensorDataModel>,
    var start: Long = sensorData.first().t_sec,
    var end: Long = sensorData.last().t_sec,
)

data class SensorDataModel(
    var t_sec: Long = System.currentTimeMillis(),
    val x_acc: Float,
    val y_acc: Float,
    val z_acc: Float,
)
