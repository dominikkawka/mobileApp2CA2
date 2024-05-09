package ie.setu.mobileapp2ca2.ui.running

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.mobileapp2ca2.firebase.FirebaseDBManager
import ie.setu.mobileapp2ca2.models.RunningModel
import ie.setu.mobileapp2ca2.firebase.FirebaseImageManager

class RunningViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addTrack(firebaseUser: MutableLiveData<FirebaseUser>,
                 running: RunningModel) {
        status.value = try {
            running.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,running)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}