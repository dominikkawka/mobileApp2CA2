package ie.setu.mobileapp2ca2.models

import androidx.lifecycle.MutableLiveData
import ie.setu.mobileapp2ca2.api.DonationClient
import ie.setu.mobileapp2ca2.api.DonationWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object DonationManager : DonationStore {

    private var donations = ArrayList<DonationModel>()

    override fun findAll(donationsList: MutableLiveData<List<DonationModel>>) {

        val call = DonationClient.getApi().findall()

        call.enqueue(object : Callback<List<DonationModel>> {
            override fun onResponse(call: Call<List<DonationModel>>,
                                    response: Response<List<DonationModel>>
            ) {
                donationsList.value = response.body() as ArrayList<DonationModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<DonationModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findAll(email: String, donationsList: MutableLiveData<List<DonationModel>>) {

        val call = DonationClient.getApi().findall(email)

        call.enqueue(object : Callback<List<DonationModel>> {
            override fun onResponse(call: Call<List<DonationModel>>,
                                    response: Response<List<DonationModel>>
            ) {
                donationsList.value = response.body() as ArrayList<DonationModel>
                Timber.i("Retrofit findAll() = ${response.body()}")
            }

            override fun onFailure(call: Call<List<DonationModel>>, t: Throwable) {
                Timber.i("Retrofit findAll() Error : $t.message")
            }
        })
    }

    override fun findById(email: String, id: String, donation: MutableLiveData<DonationModel>)   {

        val call = DonationClient.getApi().get(email,id)

        call.enqueue(object : Callback<DonationModel> {
            override fun onResponse(call: Call<DonationModel>, response: Response<DonationModel>) {
                donation.value = response.body() as DonationModel
                Timber.i("Retrofit findById() = ${response.body()}")
            }

            override fun onFailure(call: Call<DonationModel>, t: Throwable) {
                Timber.i("Retrofit findById() Error : $t.message")
            }
        })
    }

    override fun create( donation: DonationModel) {

        val call = DonationClient.getApi().post(donation.email,donation)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit ${donationWrapper.message}")
                    Timber.i("Retrofit ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }

    override fun delete(email: String,id: String) {

        val call = DonationClient.getApi().delete(email,id)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit Delete ${donationWrapper.message}")
                    Timber.i("Retrofit Delete ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Delete Error : $t.message")
            }
        })
    }

    override fun update(email: String,id: String, donation: DonationModel) {

        val call = DonationClient.getApi().put(email,id,donation)

        call.enqueue(object : Callback<DonationWrapper> {
            override fun onResponse(call: Call<DonationWrapper>,
                                    response: Response<DonationWrapper>
            ) {
                val donationWrapper = response.body()
                if (donationWrapper != null) {
                    Timber.i("Retrofit Update ${donationWrapper.message}")
                    Timber.i("Retrofit Update ${donationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<DonationWrapper>, t: Throwable) {
                Timber.i("Retrofit Update Error : $t.message")
            }
        })
    }
}
