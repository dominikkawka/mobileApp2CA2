package ie.setu.mobileapp2ca2.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface RunningStore {
    fun findAll(tracksList:
                MutableLiveData<List<RunningModel>>)
    fun findAll(userid:String,
                tracksList:
                MutableLiveData<List<RunningModel>>)
    fun findById(userid:String, runningId: String,
                 running: MutableLiveData<RunningModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, running: RunningModel)
    fun delete(userid:String, runningId: String)
    fun update(userid:String, runningId: String, running: RunningModel)
    fun filterByTitle(title: String, tracksList: MutableLiveData<List<RunningModel>>)
    fun addToFavourites(trackUid: String, userid: String)
    fun removeFromFavourites(trackUid: String, userid: String)
}