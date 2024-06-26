package ie.setu.mobileapp2ca2.ui.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import ie.setu.mobileapp2ca2.R
import ie.setu.mobileapp2ca2.models.RunningModel
import ie.setu.mobileapp2ca2.ui.auth.LoggedInViewModel
import ie.setu.mobileapp2ca2.ui.report.ReportViewModel
import ie.setu.mobileapp2ca2.utils.createLoader
import ie.setu.mobileapp2ca2.utils.hideLoader
import ie.setu.mobileapp2ca2.utils.showLoader
import timber.log.Timber

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true

            reportViewModel.observableDonationsList.observe(
                viewLifecycleOwner,
                Observer { tracks ->
                    tracks?.let {
                        render(tracks as ArrayList<RunningModel>)
                        hideLoader(loader)
                    }
                })
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loader = createLoader(requireActivity())
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private var showStart = true
    private var showEnd = true
    private fun render(tracksList: ArrayList<RunningModel>) {
        var markerColour: Float
        var markerColourEnd: Float
        if (tracksList.isNotEmpty()) {
            mapsViewModel.map.clear()
            tracksList.forEach {

                if (showStart) {
                    markerColour =
                        if (it.email.equals(this.reportViewModel.liveFirebaseUser.value!!.email))
                            BitmapDescriptorFactory.HUE_AZURE + 5
                        else
                            BitmapDescriptorFactory.HUE_RED

                    mapsViewModel.map.addMarker(
                        MarkerOptions().position(LatLng(it.startLatitude, it.startLongitude))
                            .title("${it.title} Starting Point: €${it.difficulty}")
                            .snippet(it.description)
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                    )
                }

                if (showEnd) {
                    markerColourEnd =
                        if (it.email.equals(this.reportViewModel.liveFirebaseUser.value!!.email))
                            BitmapDescriptorFactory.HUE_MAGENTA
                        else
                            BitmapDescriptorFactory.HUE_GREEN

                    mapsViewModel.map.addMarker(
                        MarkerOptions().position(LatLng(it.endLatitude, it.endLongitude))
                            .title("${it.title} Ending Point: €${it.difficulty}")
                            .snippet(it.description)
                            .icon(BitmapDescriptorFactory.defaultMarker(markerColourEnd))
                    )
                }
            }
        }

        view?.findViewById<Chip>(R.id.startChip)?.setOnClickListener {
            showStart = !showStart
            render(tracksList)
        }

        view?.findViewById<Chip>(R.id.endChip)?.setOnClickListener {
            showEnd = !showEnd
            render(tracksList)
        }

        //TODO: This function doesn't force a change in colour
        view?.findViewById<Chip>(R.id.endChip)?.setBackgroundColor(
            if (showEnd) ContextCompat.getColor(requireContext(), R.color.selected_chip)
            else ContextCompat.getColor(requireContext(), R.color.unselected_chip)
        )
    }
    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Tracks")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) {
                firebaseUser -> if (firebaseUser != null) {
            reportViewModel.liveFirebaseUser.value = firebaseUser
            reportViewModel.load()
        }        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_report, menu)

                val item = menu.findItem(R.id.toggleDonations) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleDonations: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleDonations.isChecked = false

                toggleDonations.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) reportViewModel.loadAll()
                    else reportViewModel.load()
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}
