package ie.setu.mobileapp2ca2.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.mobileapp2ca2.models.DonationManager
import ie.setu.mobileapp2ca2.models.DonationModel
import ie.setu.mobileapp2ca2.models.DonationStore
import timber.log.Timber

class DonationXApp : Application() {

    //lateinit var donationsStore: DonationStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //  donationsStore = DonationManager()
        Timber.i("DonationX Application Started")
    }
}