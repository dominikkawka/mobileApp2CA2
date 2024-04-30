package ie.setu.mobileapp2ca2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.mobileapp2ca2.models.DonationManager
import ie.setu.mobileapp2ca2.models.DonationModel
import timber.log.Timber

class DonationDetailViewModel : ViewModel() {
    private val donation = MutableLiveData<DonationModel>()

    var observableDonation: LiveData<DonationModel>
        get() = donation
        set(value) {donation.value = value.value}

    fun getDonation(email:String, id: String) {
        try {
            DonationManager.findById(email, id, donation)
            Timber.i("Detail getDonation() Success : ${donation.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getDonation() Error : $e.message")
        }
    }

    fun updateDonation(email:String, id: String,donation: DonationModel) {
        try {
            DonationManager.update(email, id, donation)
            Timber.i("Detail update() Success : $donation")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}