package com.yzh.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {
    companion object {
        const val TOKEN="XFWt8wmf2wbMrGKs"
        @SuppressLint("StaticFieldLeck")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}
