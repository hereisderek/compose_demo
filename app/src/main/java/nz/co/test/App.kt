package nz.co.test

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE
import logcat.logcat


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

        logcat {
            "logcat initialized"
        }
    }
}