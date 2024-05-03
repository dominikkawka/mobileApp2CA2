package ie.setu.mobileapp2ca2.main

import android.app.Application
import timber.log.Timber

class MobileApp2CA2App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("MobileApp2CA2 Application Started")
    }
}