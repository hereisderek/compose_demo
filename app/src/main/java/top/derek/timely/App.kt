package top.derek.timely

import android.app.Application
import android.content.Context
import logcat.AndroidLogcatLogger
import logcat.LogPriority.VERBOSE
import logcat.logcat

class App : Application() {



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = VERBOSE)

        logcat {
            "logcat initialized"
        }
    }
}