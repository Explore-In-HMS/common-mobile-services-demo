package huawei.cmsdemo.main.ui.creditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}