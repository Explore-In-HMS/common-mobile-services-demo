package huawei.cmsdemo.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        initUI ()
        return binding.root
    }

    private fun initUI () {
        binding.rvServices.adapter = ServicesAdapter(Util.servicesInfoList) {}
    }
}