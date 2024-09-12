package huawei.cmsdemo.main.ui.auth

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hms.lib.commonmobileservices.auth.AuthService
import dagger.hilt.android.AndroidEntryPoint
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentPhoneSignUpScreenBinding
import huawei.cmsdemo.main.databinding.VerifyCodeDialogBinding
import huawei.cmsdemo.main.util.Constants
import huawei.cmsdemo.main.util.Constants.COUNTRY_CODE
import huawei.cmsdemo.main.util.toastShort
import javax.inject.Inject

@AndroidEntryPoint
class PhoneSignUpScreen : Fragment() {
    @Inject
    lateinit var authService: AuthService
    private lateinit var binding: FragmentPhoneSignUpScreenBinding
    private lateinit var bindingVerify: VerifyCodeDialogBinding
    private lateinit var phoneNumber: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingVerify = VerifyCodeDialogBinding.inflate(inflater)
        binding = FragmentPhoneSignUpScreenBinding.inflate(inflater)
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.btnSignUp.setOnClickListener {
            signUpWithPhone()
        }
    }

    private fun signUpWithPhone() {
        phoneNumber = binding.etPhoneNumber.text.toString()
        password = binding.etPassword.text.toString()

        if (phoneNumber.isBlank() || password.isBlank()) {
            requireContext().toastShort(getString(R.string.phone_and_password_can_t_be_blank))
            return
        }

        // Request the verification code first
        authService.getPhoneCode(
            countryCode = Constants.COUNTRY_CODE,
            phoneNumber = phoneNumber,
            activity = requireActivity()
        )
            .addOnSuccessListener {
                showAlertDialog()  // Show dialog for entering the verification code
            }
            .addOnFailureListener {
                requireContext().toastShort(it.message.toString())
            }
    }

    private fun showAlertDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder
            .setView(bindingVerify.root)
            .setPositiveButton(getString(R.string.verify)) { dialog, _ ->
                verifyCode(dialog)
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun verifyCode(dialogInterface: DialogInterface) {
        val verificationCode = bindingVerify.etCode.text.toString()
        if (verificationCode.isBlank()) {
            requireContext().toastShort(getString(R.string.code_can_t_be_blank))
        } else {
            // Use the verification code to sign up
            authService.signUpWithPhone(
                countryCode = COUNTRY_CODE,
                phoneNumber = phoneNumber,
                password = password,
                verifyCode = verificationCode
            )
                .addOnSuccessListener {
                    requireContext().toastShort(getString(R.string.successfully_signed_up))
                    dialogInterface.dismiss()
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    requireContext().toastShort(it.message.toString())
                }
        }
    }
}