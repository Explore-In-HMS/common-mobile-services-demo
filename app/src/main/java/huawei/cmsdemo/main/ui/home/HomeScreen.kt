package huawei.cmsdemo.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import huawei.cmsdemo.main.databinding.FragmentHomeScreenBinding
import huawei.cmsdemo.main.ui.home.adapter.ServicesAdapter
import huawei.cmsdemo.main.util.Util

@AndroidEntryPoint
class HomeScreen : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater)
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.rvServices.adapter = ServicesAdapter(Util.servicesInfoList) { position ->
            onServiceClicked(position)
        }
    }

    private fun onServiceClicked(position: Int) {
        val service = Util.servicesInfoList[position]
        Toast.makeText(requireContext(), "Clicked on: ${service.servicesName}", Toast.LENGTH_SHORT)
            .show()
        if (service.navigationActionId != 0) { // If the navigationActionId is 0, the screen has not been created and navigation has not been done yet.
            findNavController().navigate(service.navigationActionId)
        }
    }
}