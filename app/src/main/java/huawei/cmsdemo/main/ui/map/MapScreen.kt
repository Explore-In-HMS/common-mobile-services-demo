package huawei.cmsdemo.main.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.databinding.FragmentMapScreenBinding


class MapScreen : Fragment() {

    private lateinit var binding: FragmentMapScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapScreenBinding.inflate(inflater)

        initMap(savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initMap(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState,lifecycle).apply {
            getMapAsync {
                it.addMarker("Marker", "Snippet", 41.0540255, 29.0129607)
                it.animateCamera(41.0540255, 29.0129607, 15f)
            }
        }
    }
}