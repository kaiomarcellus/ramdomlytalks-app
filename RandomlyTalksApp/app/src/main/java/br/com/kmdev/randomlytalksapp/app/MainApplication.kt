package br.com.kmdev.randomlytalksapp.app

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import br.com.kmdev.randomlytalksapp.BuildConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@ExperimentalCoroutinesApi
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@MainApplication)
            modules(appModules)
        }

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
        else Timber.plant(CrashReportingTree())
    }

    class CrashReportingTree : Timber.Tree() {
        override fun log(
            priority: Int,
            tag: String?, @NonNull message: String,
            t: Throwable?
        ) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
                return

            // Send your error logs to your firebase or any service
        }
    }
}