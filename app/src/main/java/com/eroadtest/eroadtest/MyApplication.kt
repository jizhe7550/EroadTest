package com.eroadtest.eroadtest

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    companion object {
        private var instance: MyApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}