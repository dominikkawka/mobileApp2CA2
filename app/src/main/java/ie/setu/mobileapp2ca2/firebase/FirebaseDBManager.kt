package ie.setu.mobileapp2ca2.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.mobileapp2ca2.models.RunningModel
import ie.setu.mobileapp2ca2.models.RunningStore
import timber.log.Timber


object FirebaseDBManager : RunningStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(tracksList: MutableLiveData<List<RunningModel>>) {
        database.child("tracks")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<RunningModel>()
                    val children = snapshot.children
                    children.forEach {
                        val track = it.getValue(RunningModel::class.java)
                        localList.add(track!!)
                    }
                    database.child("tracks")
                        .removeEventListener(this)

                    tracksList.value = localList
                }
            })
    }

    override fun findAll(userid: String, tracksList: MutableLiveData<List<RunningModel>>) {

        database.child("user-tracks").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<RunningModel>()
                    val children = snapshot.children
                    children.forEach {
                        val track = it.getValue(RunningModel::class.java)
                        localList.add(track!!)
                    }
                    database.child("user-tracks").child(userid)
                        .removeEventListener(this)

                    tracksList.value = localList
                }
            })
    }

    override fun findById(userid: String, trackid: String, track: MutableLiveData<RunningModel>) {

        database.child("user-tracks").child(userid)
            .child(trackid).get().addOnSuccessListener {
                track.value = it.getValue(RunningModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, track: RunningModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("tracks").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        track.uid = key
        val trackValues = track.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/tracks/$key"] = trackValues
        childAdd["/user-tracks/$uid/$key"] = trackValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, trackid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/tracks/$trackid"] = null
        childDelete["/user-tracks/$userid/$trackid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, trackid: String, track: RunningModel) {

        val trackValues = track.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["tracks/$trackid"] = trackValues
        childUpdate["user-tracks/$userid/$trackid"] = trackValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userTracks = database.child("user-tracks").child(userid)
        val allTracks = database.child("tracks")

        userTracks.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all donations that match 'it'
                        val track = it.getValue(RunningModel::class.java)
                        allTracks.child(track!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}