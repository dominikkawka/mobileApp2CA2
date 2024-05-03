package ie.setu.mobileapp2ca2.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface RunningStore {
    fun findAll(donationsList:
                MutableLiveData<List<RunningModel>>)
    fun findAll(userid:String,
                donationsList:
                MutableLiveData<List<RunningModel>>)
    fun findById(userid:String, donationid: String,
                 donation: MutableLiveData<RunningModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, donation: RunningModel)
    fun delete(userid:String, donationid: String)
    fun update(userid:String, donationid: String, donation: RunningModel)
}