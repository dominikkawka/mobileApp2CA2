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
import ie.setu.mobileapp2ca2.databinding.FragmentRunningDetailBinding
import ie.setu.mobileapp2ca2.ui.auth.LoggedInViewModel
import ie.setu.mobileapp2ca2.ui.report.ReportViewModel

class RunningDetailFragment : Fragment() {

    private lateinit var detailViewModel: RunningDetailViewModel
    private val args by navArgs<RunningDetailFragmentArgs>()
    private var _fragBinding: FragmentRunningDetailBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val reportViewModel : ReportViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentRunningDetailBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.editDonationButton.setOnClickListener {
            reportViewModel.update(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.runningid, fragBinding.runningvm?.observableTrack!!.value!!)
            findNavController().navigateUp()
        }

        fragBinding.deleteDonationButton.setOnClickListener {
            reportViewModel.delete(loggedInViewModel.liveFirebaseUser.value?.email!!,
                detailViewModel.observableTrack.value?.uid!!)
            findNavController().navigateUp()

            //detailViewModel.deleteTrack(loggedInViewModel.liveFirebaseUser.value?.email!!,
            //    detailViewModel.observableTrack.value?.uid!!)
            //findNavController().navigateUp()
        }

        fragBinding.addFavouriteButton.setOnClickListener {
            reportViewModel.addToFavourites(loggedInViewModel.liveFirebaseUser.value?.uid!!, detailViewModel.observableTrack.value?.uid!!)
        }

        fragBinding.removeFavouriteButton.setOnClickListener {
            reportViewModel.removeFromFavourites(loggedInViewModel.liveFirebaseUser.value?.uid!!, detailViewModel.observableTrack.value?.uid!!)
        }

        detailViewModel = ViewModelProvider(this).get(RunningDetailViewModel::class.java)
        detailViewModel.observableTrack.observe(viewLifecycleOwner, Observer { render() })

        return root
    }

    private fun render() {
        val track = detailViewModel.observableTrack.value
        track?.let { // Safely access track
            val distanceInKm = it.distance / 1000.0
            val formattedDistance = String.format("%.3f", distanceInKm)
            fragBinding.editTextDistance.setText("$formattedDistance km")
            fragBinding.editTextWeather.setText(it.weatherCondition)
            fragBinding.editTextDifficulty.setText(it.difficulty.toString())
            fragBinding.runningvm = detailViewModel
        }
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