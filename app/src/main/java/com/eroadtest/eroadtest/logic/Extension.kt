package com.eroadtest.eroadtest.logic

import android.hardware.SensorEvent
import com.eroadtest.eroadtest.model.SensorDataModel

fun SensorEvent.toSensorDataModel(): SensorDataModel {
    val x = this.values[0]
    val y = this.values[1]
    val z = this.values[2]
    return SensorDataModel(x_acc = x, y_acc = y, z_acc = z)
}
