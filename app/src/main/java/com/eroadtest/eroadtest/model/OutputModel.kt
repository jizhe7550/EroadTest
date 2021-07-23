package com.eroadtest.eroadtest.model

data class OutputModel(
    var sensorData: List<SensorDataModel>,
    var start: Long = sensorData.first().t_sec,
    var end: Long = sensorData.last().t_sec,
)
