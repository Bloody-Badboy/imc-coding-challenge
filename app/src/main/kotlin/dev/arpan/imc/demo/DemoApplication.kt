package dev.arpan.imc.demo

import android.app.Application
import android.os.StrictMode
import dev.arpan.imc.demo.di.initKoin
import org.koin.core.context.stopKoin
import timber.log.Timber

class DemoApplication : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }
        super.onCreate()
        initKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return "IMC_" + super.createStackElementTag(element)
                }
            })
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .detectNetwork()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build()
        )
    }
}
