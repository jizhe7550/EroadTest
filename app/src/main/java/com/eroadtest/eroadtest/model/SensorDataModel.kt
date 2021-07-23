package com.eroadtest.eroadtest.model

data class SensorDataModel(
    var t_sec: Long = System.currentTimeMillis(),
    val x_acc: Float,
    val y_acc: Float,
    val z_acc: Float,
)
