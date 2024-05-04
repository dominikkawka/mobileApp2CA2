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

class RunningFragment : Fragment() {

    var totalTracks = 0
    private var _fragBinding: FragmentRunningBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    private lateinit var donateViewModel: RunningViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentRunningBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        // activity?.title = getString(R.string.action_donate)
        setupMenu()
        donateViewModel = ViewModelProvider(this).get(RunningViewModel::class.java)
        donateViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })

        fragBinding.progressBar.max = 10000
        fragBinding.amountPicker.minValue = 1
        fragBinding.amountPicker.maxValue = 1000

        fragBinding.amountPicker.setOnValueChangedListener { _, _, newVal ->
            //Display the newly selected number to paymentAmount
            fragBinding.paymentAmount.setText("$newVal")
        }
        setButtonListener(fragBinding)
        return root;
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }

            false -> Toast.makeText(context, getString(R.string.donationError), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setButtonListener(layout: FragmentRunningBinding) {
        layout.donateButton.setOnClickListener {
            val amount = if (layout.paymentAmount.text.isNotEmpty())
                layout.paymentAmount.text.toString().toInt() else layout.amountPicker.value
            if (totalTracks >= layout.progressBar.max)
                Toast.makeText(context, "Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            else {
                val paymentmethod =
                    if (layout.paymentMethod.checkedRadioButtonId == R.id.Direct) "Direct" else "Paypal"
                totalTracks += amount
                layout.totalSoFar.text = getString(R.string.total_donated, totalTracks)
                layout.progressBar.progress = totalTracks
                donateViewModel.addDonation(loggedInViewModel.liveFirebaseUser,
                    RunningModel(title = paymentmethod,difficulty = amount,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                        startLatitude = mapsViewModel.currentLocation.value!!.latitude,
                        startLongitude = mapsViewModel.currentLocation.value!!.longitude))
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_donate, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
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
            totalTracks = reportViewModel.observableDonationsList.value!!.sumBy { it.difficulty }
            fragBinding.progressBar.progress = totalTracks
            fragBinding.totalSoFar.text = getString(R.string.total_donated, totalTracks)
        })
    }
}