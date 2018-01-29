package com.example.applemacbookproretina.learningandroid1

import android.app.Application
import android.content.Context

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        AppContext.appContext = getApplicationContext()
    }

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }

    companion object {

        var appContext: Context? = null
            private set
    }
}