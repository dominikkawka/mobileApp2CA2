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

class RunningDetailFragment : Fragment() {

    private lateinit var detailViewModel: RunningDetailViewModel
    private val args by navArgs<RunningDetailFragmentArgs>()
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
            detailViewModel.updateTrack(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.runningid, fragBinding.runningvm?.observableDonation!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteDonationButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableDonation.value?.uid!!)
            findNavController().navigateUp()
        }

        fragBinding.addFavouriteButton.setOnClickListener {
            reportViewModel.addToFavourites(loggedInViewModel.liveFirebaseUser.value?.uid!!, detailViewModel.observableDonation.value?.uid!!)
        }

        fragBinding.removeFavouriteButton.setOnClickListener {
            reportViewModel.removeFromFavourites(loggedInViewModel.liveFirebaseUser.value?.uid!!, detailViewModel.observableDonation.value?.uid!!)
        }

        detailViewModel = ViewModelProvider(this).get(RunningDetailViewModel::class.java)
        detailViewModel.observableDonation.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render() {
        fragBinding.editMessage.setText("A Message")
        fragBinding.editUpvotes.setText("0")
        fragBinding.runningvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getTrack(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.runningid)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}