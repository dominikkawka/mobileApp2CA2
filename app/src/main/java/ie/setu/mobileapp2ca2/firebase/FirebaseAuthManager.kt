package ie.setu.mobileapp2ca2.firebase

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

//https://tutors.dev/lab/mobile-app-dev-2-2024/topic-10-fb-auth/unit-01/book-e-donationx-v5/03

class FirebaseAuthManager(application: Application) {

    private var application: Application? = null

    var firebaseAuth: FirebaseAuth? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()
    var errorStatus = MutableLiveData<Boolean>()

    init {
        this.application = application
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
            errorStatus.postValue(false)
        }
    }

    fun login(email: String?, password: String?) {
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    Timber.i("Login Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun register(email: String?, password: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(application!!.mainExecutor) { task ->
                if (task.isSuccessful) {
                    liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
                    errorStatus.postValue(false)
                } else {
                    Timber.i("Registration Failure: $task.exception!!.message")
                    errorStatus.postValue(true)
                }
            }
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        loggedOut.postValue(true)
        errorStatus.postValue(false)
    }
}