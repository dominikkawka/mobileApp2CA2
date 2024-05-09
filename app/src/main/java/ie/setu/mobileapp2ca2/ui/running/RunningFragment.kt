package ie.setu.mobileapp2ca2.ui.running

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.setu.mobileapp2ca2.R
import ie.setu.mobileapp2ca2.databinding.FragmentRunningBinding
import ie.setu.mobileapp2ca2.models.RunningModel
import ie.setu.mobileapp2ca2.ui.auth.LoggedInViewModel
import ie.setu.mobileapp2ca2.ui.map.MapsViewModel
import ie.setu.mobileapp2ca2.ui.report.ReportViewModel
import android.location.Location.distanceBetween
import timber.log.Timber

class RunningFragment : Fragment() {

    private var totalTracks = 0
    private var _fragBinding: FragmentRunningBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var runningViewModel: RunningViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()

    private var distanceResults = FloatArray(1)
    private var startLat = 0.0
    private var startLong = 0.0
    private var endLat = 0.0
    private var endLong = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunningBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        runningViewModel = ViewModelProvider(this).get(RunningViewModel::class.java)
        runningViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        fragBinding.difficultyPicker.minValue = 1
        fragBinding.difficultyPicker.maxValue = 5
        setButtonListener(fragBinding)

        val displayName = loggedInViewModel.liveFirebaseUser.value?.displayName
        fragBinding.welcomeUserText.text = "Welcome ${displayName ?: ""}"

        fragBinding.startTrackButton.setOnClickListener {
            startLat = mapsViewModel.currentLocation.value!!.latitude
            startLong = mapsViewModel.currentLocation.value!!.longitude
            Timber.i("Start $startLat, $startLong")
        }

        fragBinding.endTrackButton.setOnClickListener {
            endLat = mapsViewModel.currentLocation.value!!.latitude
            endLong = mapsViewModel.currentLocation.value!!.longitude
            Timber.i("End $endLat, $endLong")
            Timber.i("Start at End $startLat, $startLong")
        }

        return root
    }

    private fun render(status: Boolean) {
        if (!status) {
            Toast.makeText(context, getString(R.string.donationError), Toast.LENGTH_LONG).show()
        }
    }

    private fun setButtonListener(layout: FragmentRunningBinding) {
        layout.donateButton.setOnClickListener {

            val runningWeather = when (layout.runningWeather.checkedRadioButtonId) {
                R.id.Clear -> "Clear"
                R.id.Sunny -> "Sunny"
                R.id.Cloudy -> "Cloudy"
                R.id.Rainy -> "Rainy"
                else -> "Unknown"
            }

            val difficulty = layout.difficultyPicker.value
            if (layout.runningTitle.text.toString().isEmpty()) {
                Toast.makeText(context, "Please name your track", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (startLat == 0.0 || startLong == 0.0) {
                Toast.makeText(context, "Please begin your run before submitting", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (endLat == 0.0 || endLong == 0.0) {
                Toast.makeText(context, "Please finish your run before submitting", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            distanceBetween(startLat, startLong, endLat, endLong, distanceResults)
            runningViewModel.addTrack(
                loggedInViewModel.liveFirebaseUser,
                RunningModel(
                    title = layout.runningTitle.text.toString(),
                    weatherCondition = runningWeather,
                    difficulty = difficulty,
                    email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                    startLatitude = startLat,
                    startLongitude = startLong,
                    endLatitude = endLat,
                    endLongitude = endLong,
                    distance = distanceResults[0]
                )
            )
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {}

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_donate, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
        reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
            totalTracks = reportViewModel.observableDonationsList.value?.sumBy { it.difficulty } ?: 0
        })
    }
}