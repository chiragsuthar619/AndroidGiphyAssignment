package com.example.testoftest.storage.database

import android.content.Context
import com.example.testoftest.storage.entity.MyObjectBox
import io.objectbox.BoxStore

object ObjectBox {
    var boxStore: BoxStore? = null
        private set

    fun init(context: Context) {
        if(boxStore == null){
            boxStore = MyObjectBox.builder()
                .androidContext(context.applicationContext)
                .build()
        }

    }
}