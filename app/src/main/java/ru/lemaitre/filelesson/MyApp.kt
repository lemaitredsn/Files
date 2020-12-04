package ru.lemaitre.filelesson

import android.app.Application
import android.content.Context

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MyApp.context = applicationContext
    }

    companion object{
        lateinit var context: Context
        fun getAppContext() = MyApp.context

    }
}