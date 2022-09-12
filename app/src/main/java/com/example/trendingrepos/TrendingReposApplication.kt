package com.example.trendingrepos

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TrendingReposApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}