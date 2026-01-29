package com.example.otpauth

import android.app.Application
import com.example.otpauth.analytics.AnalyticsLogger
import timber.log.Timber

class OtpAuthApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AnalyticsLogger.init()
    }
}
