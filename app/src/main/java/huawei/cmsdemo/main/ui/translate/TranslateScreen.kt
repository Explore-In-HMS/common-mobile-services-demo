package huawei.cmsdemo.main.ui.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.hms.lib.commonmobileservices.translate.Translator
import com.hms.lib.commonmobileservices.translate.common.DownloadModelResult
import com.hms.lib.commonmobileservices.translate.common.RequiresModelDownloadResult
import com.hms.lib.commonmobileservices.translate.common.TranslateResult
import com.hms.lib.commonmobileservices.translate.implementation.ITranslator
import dagger.hilt.android.AndroidEntryPoint
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentTranslateScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog
import huawei.cmsdemo.main.util.toastShort

@AndroidEntryPoint
class TranslateScreen : Fragment() {
    private lateinit var binding: FragmentTranslateScreenBinding
    private lateinit var translator: ITranslator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateScreenBinding.inflate(inflater)
        showAlertDialog()

        translator = Translator.getClient(requireContext())
        requiresModelDownload(getString(R.string.source_lang), binding.ivSourceLang)
        requiresModelDownload(getString(R.string.target_lang), binding.ivTargetLang)
        initUI()
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

    private fun initUI() {
        with(binding) {
                btnTranslate.setOnClickListener {
                    translate()
                }
                ivSourceLang.setOnClickListener {
                    downloadModel(getString(R.string.source_lang))
                }
                ivTargetLang.setOnClickListener {
                    downloadModel(getString(R.string.target_lang))
                }
        }
    }
    private fun translate(){
        val text = binding.etTextField.text.toString()
        if (text.isBlank()) {
            requireContext().toastShort(getString(R.string.text_can_t_be_blank))
            return
        }
        translator.translate(text,getString(R.string.source_lang),getString(R.string.target_lang)){ translateResult ->
            when(translateResult){
                is TranslateResult.Success -> {
                    binding.tvTranslateResult.text = translateResult.translatedText
                }
                is TranslateResult.Error -> {
                    requireContext().toastShort(translateResult.exception.message.toString())
                }
            }
        }
    }

    private fun requiresModelDownload(lang: String, imageView: ImageView) {
        translator.requiresModelDownload(lang){ requiresModelDownloadResult ->
            when(requiresModelDownloadResult){
                is RequiresModelDownloadResult.Required -> {
                    imageView.visibility = View.VISIBLE
                    requireContext().toastShort(getString(R.string.required, lang))
                }
                is RequiresModelDownloadResult.NotRequired -> {
                    imageView.visibility = View.INVISIBLE
                    requireContext().toastShort(getString(R.string.not_required, lang))
                }
                is RequiresModelDownloadResult.Error -> {
                    requireContext().toastShort(requiresModelDownloadResult.exception.message.toString())
                }
            }
        }
    }

    private fun downloadModel(lang: String) {
        showProgress()
        translator.downloadModel(lang){ downloadModelResult ->
            when(downloadModelResult){
                is DownloadModelResult.Success -> {
                    //Model Downloaded
                    hideProgress()
                    requireContext().toastShort(getString(R.string.model_downloaded, lang))
                }
                is DownloadModelResult.Error -> {
                    //Handle exception -> downloadModelResult.exception
                    requireContext().toastShort(downloadModelResult.exception.message.toString())
                    hideProgress()
                }
            }
        }
    }

    private fun showProgress() {
        with(binding) {
            progressBar.isVisible = true
            btnTranslate.isEnabled = false
        }
    }

    private fun hideProgress() {
        with(binding) {
            progressBar.isVisible = false
            btnTranslate.isEnabled = true
        }
    }
}