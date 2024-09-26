package huawei.cmsdemo.main.ui.creditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.creditcardscanner.CreditCardScanner
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentCreditCardScannerScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog

class CreditCardScannerScreen : Fragment() {
    private lateinit var binding: FragmentCreditCardScannerScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditCardScannerScreenBinding.inflate(inflater)
        showAlertDialog()
        initUI()
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.credit_card_scanner) }
        with(servicesInfo) {
            requireContext().showAlertDialog(
                title = servicesName,
                desc = desc,
                versions = version
            )
        }
    }

    private fun initUI() {
        binding.btnStartScan.setOnClickListener {
            scan()
        }
    }

    private fun scan() {
        CreditCardScanner.instance(requireContext())?.scan {
            when (it) {
                is ResultData.Success -> {
                    binding.tvScanResult.text = getString(
                        R.string.scan_result,
                        it.data?.cardNumber,
                        it.data?.expireDate
                    )
                    hideProgress()
                }

                is ResultData.Failed -> {
                    binding.tvScanResult.text = it.error
                    hideProgress()
                }

                is ResultData.Loading -> {
                    showProgress()
                }
            }

        }
    }

    private fun showProgress() {
        with(binding) {
            progressBar.isVisible = true
        }
    }

    private fun hideProgress() {
        with(binding) {
            progressBar.isVisible = false
        }
    }
}