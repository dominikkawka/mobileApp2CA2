package ie.setu.mobileapp2ca2.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.mobileapp2ca2.firebase.FirebaseDBManager
import ie.setu.mobileapp2ca2.models.DonationManager
import ie.setu.mobileapp2ca2.models.DonationModel
import ie.wit.donationx.firebase.FirebaseImageManager

class DonateViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addDonation(firebaseUser: MutableLiveData<FirebaseUser>,
                    donation: DonationModel) {
        status.value = try {
            //DonationManager.create(donation)
            donation.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,donation)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}