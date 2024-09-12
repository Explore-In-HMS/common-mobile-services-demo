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
import com.hms.lib.commonmobileservices.auth.common.VerificationType
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentSignUpScreenBinding
import huawei.cmsdemo.main.databinding.VerifyCodeDialogBinding
import huawei.cmsdemo.main.util.toastShort
import javax.inject.Inject


class SignUpScreen : Fragment() {
    @Inject
    lateinit var authService: AuthService
    private lateinit var binding: FragmentSignUpScreenBinding
    private lateinit var bindingVerify: VerifyCodeDialogBinding
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingVerify = VerifyCodeDialogBinding.inflate(inflater)
        binding = FragmentSignUpScreenBinding.inflate(inflater)
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            requireContext().toastShort(getString(R.string.email_and_password_can_t_be_blank))
            return
        }

        authService.signUp(email, password)
            .addOnSuccessListener {
                if (it == VerificationType.CODE) {
                    showAlertDialog()
                } else if (it == VerificationType.NON) {
                    requireContext().toastShort(getString(R.string.successfully_signed_up))
                    findNavController().popBackStack()
                }
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
            authService.verifyCode(email, password, verificationCode)
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