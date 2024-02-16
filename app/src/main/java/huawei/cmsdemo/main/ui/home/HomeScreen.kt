package huawei.cmsdemo.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.databinding.FragmentHomeScreenBinding

class HomeScreen : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater)
        return binding.root
    }

}