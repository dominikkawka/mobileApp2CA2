package ie.setu.mobileapp2ca2.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.mobileapp2ca2.models.DonationManager
import ie.setu.mobileapp2ca2.models.DonationModel

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDonation(donation: DonationModel) {
        status.value = try {
            DonationManager.create(donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}