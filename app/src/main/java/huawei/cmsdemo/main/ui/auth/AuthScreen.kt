package huawei.cmsdemo.main.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentAuthScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog

class AuthScreen : Fragment() {
    private lateinit var binding: FragmentAuthScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthScreenBinding.inflate(inflater)
        showAlertDialog()
        initUI()
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.auth_service) }
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
            btnEmailPassword.setOnClickListener {
                findNavController().navigate(R.id.action_authScreen_to_emailPasswordLogin)
            }
            btnPhoneSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_authScreen_to_phoneSignInScreen)
            }
        }
    }
}