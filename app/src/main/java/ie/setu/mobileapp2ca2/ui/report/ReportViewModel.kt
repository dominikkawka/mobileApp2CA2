package ie.setu.mobileapp2ca2.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.mobileapp2ca2.firebase.FirebaseDBManager
import ie.setu.mobileapp2ca2.models.RunningModel
import timber.log.Timber

class ReportViewModel : ViewModel() {

    private val tracksList =
        MutableLiveData<List<RunningModel>>()

    val observableDonationsList: LiveData<List<RunningModel>>
        get() = tracksList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    init { load() }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, tracksList)
            Timber.i("Report Load Success : ${tracksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

    fun update(userid:String, id: String, running: RunningModel) {
        try {
            FirebaseDBManager.update(userid, id, running)
            Timber.i("Detail update() Success : $running")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(tracksList)
            Timber.i("Report LoadAll Success : ${tracksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun filter(title: String) {
        try {
            FirebaseDBManager.filterByTitle(title, tracksList)
            Timber.i("Report LoadAll Success : ${tracksList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun addToFavourites(trackUid: String, userid: String) {
        try {
            FirebaseDBManager.addToFavourites(trackUid, userid)
            Timber.i("Add to favorites Success")
        } catch (e: Exception) {
            Timber.i("Add to favorites Error : $e.message")
        }
    }

    fun removeFromFavourites(trackUid: String, userid: String) {
        try {
            FirebaseDBManager.removeFromFavourites(trackUid, userid)
            Timber.i("Remove from favorites Success")
        } catch (e: Exception) {
            Timber.i("Remove from favorites Error : $e.message")
        }
    }
}