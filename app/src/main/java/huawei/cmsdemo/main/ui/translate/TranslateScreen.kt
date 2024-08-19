package huawei.cmsdemo.main.ui.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import huawei.cmsdemo.main.databinding.FragmentTranslateScreenBinding


class TranslateScreen : Fragment() {
    private lateinit var binding: FragmentTranslateScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateScreenBinding.inflate(inflater)
        // Inflate the layout for this fragment
        return binding.root
    }
}