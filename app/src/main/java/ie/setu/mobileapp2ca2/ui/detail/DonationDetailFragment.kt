package ie.setu.mobileapp2ca2.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.mobileapp2ca2.databinding.FragmentDonationDetailBinding
import ie.setu.mobileapp2ca2.ui.auth.LoggedInViewModel
import ie.setu.mobileapp2ca2.ui.report.ReportViewModel

class DonationDetailFragment : Fragment() {

    private lateinit var detailViewModel: DonationDetailViewModel
    private val args by navArgs<DonationDetailFragmentArgs>()
    private var _fragBinding: FragmentDonationDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.editDonationButton.setOnClickListener {
            detailViewModel.updateDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.donationid, fragBinding.donationvm?.observableDonation!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteDonationButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableDonation.value?.uid!!)
            findNavController().navigateUp()
        }

        detailViewModel = ViewModelProvider(this).get(DonationDetailViewModel::class.java)
        detailViewModel.observableDonation.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render(/*donation: DonationModel*/) {
        // fragBinding.editAmount.setText(donation.amount.toString())
        // fragBinding.editPaymenttype.text = donation.paymentmethod
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.donationvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getDonation(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.donationid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}