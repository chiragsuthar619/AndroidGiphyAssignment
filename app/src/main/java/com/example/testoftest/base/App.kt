package com.example.testoftest.base

import android.app.Application
import com.example.testoftest.storage.database.ObjectBox

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)


    }
}