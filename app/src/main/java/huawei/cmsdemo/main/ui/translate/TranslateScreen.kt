package huawei.cmsdemo.main.ui.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentTranslateScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog


class TranslateScreen : Fragment() {
    private lateinit var binding: FragmentTranslateScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateScreenBinding.inflate(inflater)
        showAlertDialog()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.translate_kit) }
        with(servicesInfo) {
            requireContext().showAlertDialog(
                title = servicesName,
                desc = desc,
                versions = version
            )
        }
    }
}