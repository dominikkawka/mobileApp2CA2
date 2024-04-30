package ie.setu.mobileapp2ca2.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.mobileapp2ca2.models.DonationManager
import ie.setu.mobileapp2ca2.models.DonationModel
import timber.log.Timber

class ReportViewModel : ViewModel() {

    private val donationsList =
        MutableLiveData<List<DonationModel>>()

    val observableDonationsList: LiveData<List<DonationModel>>
        get() = donationsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            Timber.i("Report Load Success : ${donationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(email: String, id: String) {
        try {
            DonationManager.delete(email,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}