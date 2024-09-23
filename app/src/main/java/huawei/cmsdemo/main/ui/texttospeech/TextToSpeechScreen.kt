package huawei.cmsdemo.main.ui.texttospeech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentTexttospeechScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog

class TextToSpeechScreen : Fragment() {
    private lateinit var binding: FragmentTexttospeechScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTexttospeechScreenBinding.inflate(inflater)
        showAlertDialog()
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.text_to_speech) }
        with(servicesInfo) {
            requireContext().showAlertDialog(
                title = servicesName,
                desc = desc,
                versions = version
            )
        }
    }
}