package ie.setu.mobileapp2ca2.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.setu.mobileapp2ca2.api.RunningClient
import ie.setu.mobileapp2ca2.api.RunningWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object RunningManager : RunningStore {

    private var tracks = ArrayList<RunningModel>()

    override fun findAll(tracksList: MutableLiveData<List<RunningModel>>) {

        val call = RunningClient.getApi().findall()

        call.enqueue(object : Callback<List<RunningModel>> {
            override fun onResponse(call: Call<List<RunningModel>>,
                                    response: Response<List<RunningModel>>
            ) {
                tracksList.value = response.body() as ArrayList<RunningModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<RunningModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findAll(email: String, tracksList: MutableLiveData<List<RunningModel>>) {

        val call = RunningClient.getApi().findall(email)

        call.enqueue(object : Callback<List<RunningModel>> {
            override fun onResponse(call: Call<List<RunningModel>>,
                                    response: Response<List<RunningModel>>
            ) {
                tracksList.value = response.body() as ArrayList<RunningModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<RunningModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findById(email: String, id: String, track: MutableLiveData<RunningModel>)   {

        val call = RunningClient.getApi().get(email,id)

        call.enqueue(object : Callback<RunningModel> {
            override fun onResponse(call: Call<RunningModel>, response: Response<RunningModel>) {
                track.value = response.body() as RunningModel
                Timber.i("Retrofit findById() = ${response.body()}")
            }

            override fun onFailure(call: Call<RunningModel>, t: Throwable) {
                Timber.i("Retrofit findById() Error : $t.message")
            }
        })
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, track: RunningModel) {

        val call = RunningClient.getApi().post(track.email,track)

        call.enqueue(object : Callback<RunningWrapper> {
            override fun onResponse(call: Call<RunningWrapper>,
                                    response: Response<RunningWrapper>
            ) {
                val trackWrapper = response.body()
                if (trackWrapper != null) {
                    Timber.i("Retrofit ${trackWrapper.message}")
                    Timber.i("Retrofit ${trackWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<RunningWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }

    override fun delete(email: String,id: String) {

        val call = RunningClient.getApi().delete(email,id)

        call.enqueue(object : Callback<RunningWrapper> {
            override fun onResponse(call: Call<RunningWrapper>,
                                    response: Response<RunningWrapper>
            ) {
                val trackWrapper = response.body()
                if (trackWrapper != null) {
                    Timber.i("Retrofit Delete ${trackWrapper.message}")
                    Timber.i("Retrofit Delete ${trackWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<RunningWrapper>, t: Throwable) {
                Timber.i("Retrofit Delete Error : $t.message")
            }
        })
    }

    override fun update(email: String,id: String, track: RunningModel) {

        val call = RunningClient.getApi().put(email,id,track)

        call.enqueue(object : Callback<RunningWrapper> {
            override fun onResponse(call: Call<RunningWrapper>,
                                    response: Response<RunningWrapper>
            ) {
                val trackWrapper = response.body()
                if (trackWrapper != null) {
                    Timber.i("Retrofit Update ${trackWrapper.message}")
                    Timber.i("Retrofit Update ${trackWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<RunningWrapper>, t: Throwable) {
                Timber.i("Retrofit Update Error : $t.message")
            }
        })
    }

    override fun filterByTitle(title: String, tracksList: MutableLiveData<List<RunningModel>>) {
        TODO("Not yet implemented")
    }
}

