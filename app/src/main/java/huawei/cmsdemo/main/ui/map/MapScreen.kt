package huawei.cmsdemo.main.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hms.lib.commonmobileservices.location.CommonLocationClient
import com.hms.lib.commonmobileservices.location.factory.LocationFactory
import com.hms.lib.commonmobileservices.location.model.EnableGPSFinalResult
import com.hms.lib.commonmobileservices.location.model.LocationResultState
import com.hms.lib.commonmobileservices.mapkit.factory.CommonMap
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentMapScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog
import huawei.cmsdemo.main.util.toastShort

class MapScreen : Fragment() {

    private lateinit var binding: FragmentMapScreenBinding
    private lateinit var mapView: CommonMap
    private var locationClient: CommonLocationClient? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapScreenBinding.inflate(inflater)
        val servicesInfo = Util.servicesInfoList.first { it.servicesName == getString(R.string.map_location_kit) }
        with(servicesInfo) {
            requireContext().showAlertDialog(
                title = servicesName,
                desc = desc,
                versions = version
            )
        }
        mapView = binding.mapView.onCreate(savedInstanceState, lifecycle)
        initLocation()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initMap(latitude: Double, longitude: Double) {
        mapView.apply {
            getMapAsync {
                it.addMarker(
                    getString(R.string.marker),
                    getString(R.string.snippet), latitude, longitude
                )
                it.animateCamera(latitude, longitude, 15f)
            }
        }
    }

    private fun initLocation() {
        locationClient = LocationFactory.getLocationClient(requireActivity(), lifecycle)

        locationClient?.enableGps { enableGPSFinalResult, error ->
            when (enableGPSFinalResult) {
                EnableGPSFinalResult.ENABLED -> {
                    requireContext().toastShort(getString(R.string.gps_enabled))
                    requestLocationUpdates()
                }

                EnableGPSFinalResult.FAILED -> requireContext().toastShort(getString(R.string.gps_enabling_failed) + "$error")

                EnableGPSFinalResult.USER_CANCELLED -> requireContext().toastShort(getString(R.string.gps_enabling_cancelled))
            }
        }
    }

    private fun requestLocationUpdates() {
        locationClient?.requestLocationUpdates { requestResult ->
            when (requestResult.state) {
                LocationResultState.SUCCESS -> {
                    requireContext().toastShort(getString(R.string.request_location_updates_is_successful))
                    getLastLocation()
                }

                LocationResultState.FAIL -> requireContext().toastShort(getString(R.string.failed_to_request_location_updates))

                LocationResultState.LOCATION_UNAVAILABLE -> requireContext().toastShort(getString(R.string.location_unavailable_location_updates))

                LocationResultState.GPS_DISABLED -> requireContext().toastShort(getString(R.string.gps_disabled_location_updates))

                LocationResultState.NO_LAST_LOCATION -> requireContext().toastShort(getString(R.string.no_last_location_location_updates))
            }
        }
    }
    private fun getLastLocation() {
        locationClient?.getLastKnownLocation { lastKnownLocationResult ->
            when (lastKnownLocationResult.state) {
                LocationResultState.SUCCESS -> {
                    val latitude = lastKnownLocationResult.location?.latitude
                    val longitude = lastKnownLocationResult.location?.longitude
                    if (latitude != null && longitude != null) {
                        initMap(latitude, longitude)
                    }
                    requireContext().toastShort(
                        getString(R.string.latitude) + "$latitude \n" + getString(
                            R.string.longitude
                        ) + "$longitude"
                    )
                    removeLocationUpdates()
                }

                LocationResultState.FAIL -> requireContext().toastShort(getString(R.string.failed_to_get_last_known_location))

                LocationResultState.LOCATION_UNAVAILABLE -> requireContext().toastShort(
                    getString(
                        R.string.location_unavailable_last_location
                    )
                )

                LocationResultState.GPS_DISABLED -> requireContext().toastShort(
                    getString(
                        R.string.gps_disabled_last_location
                    )
                )

                LocationResultState.NO_LAST_LOCATION -> requireContext().toastShort(
                    getString(
                        R.string.no_last_location_last_location
                    )
                )
            }
        }
    }

    private fun removeLocationUpdates() {
        locationClient?.removeLocationUpdates()
    }

}