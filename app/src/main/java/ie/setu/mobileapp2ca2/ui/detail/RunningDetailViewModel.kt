package ie.setu.mobileapp2ca2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.mobileapp2ca2.firebase.FirebaseDBManager
import ie.setu.mobileapp2ca2.models.RunningModel
import timber.log.Timber

class RunningDetailViewModel : ViewModel() {
    private val running = MutableLiveData<RunningModel>()

    var observableDonation: LiveData<RunningModel>
        get() = running
        set(value) {running.value = value.value}

    fun getTrack(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, running)
            Timber.i("Detail getTrack() Success : ${
                running.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getTrack() Error : $e.message")
        }
    }

    fun updateTrack(userid:String, id: String, running: RunningModel) {
        try {
            FirebaseDBManager.update(userid, id, running)
            Timber.i("Detail update() Success : $running")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}