package com.sklarskyj.activitylauncher

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import com.sklarskyj.activitylauncher.services.SettingsService
import javax.inject.Inject

@HiltAndroidApp
class ActivityLauncherApp : MultiDexApplication() {
    @Inject
    internal lateinit var settingsService: SettingsService

    override fun onCreate() {
        super.onCreate()

        settingsService.init()
    }
}