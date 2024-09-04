package huawei.cmsdemo.main.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hms.lib.commonmobileservices.auth.AuthService
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentEmailPasswordLoginBinding
import huawei.cmsdemo.main.util.toastShort
import javax.inject.Inject

class EmailPasswordScreen : Fragment() {
    @Inject
    lateinit var authService: AuthService
    private lateinit var binding: FragmentEmailPasswordLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailPasswordLoginBinding.inflate(inflater)
        initUI()
        checkUser()
        return binding.root
    }

    private fun initUI() {
        with(binding) {
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_emailPasswordLogin_to_signUpScreen)
            }
            btnSignIn.setOnClickListener {
                signInWithEmail()
            }
            tvSignOut.setOnClickListener {
                signOut()
            }
        }
    }

    private fun signInWithEmail() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isBlank() || password.isBlank()) {
            requireContext().toastShort(getString(R.string.email_and_password_can_t_be_blank))
            return
        }

        authService.signInWithEmail(email, password)
            .addOnSuccessListener { authUser ->
                with(binding) {
                    etEmail.text?.clear()
                    etPassword.text?.clear()
                    tvWelcomeUser.text = getString(R.string.welcome_user_id, authUser.id)
                }
                requireContext().toastShort(getString(R.string.successfully_sign_in))

            }
            .addOnFailureListener {
                requireContext().toastShort(it.message.toString())
            }
    }

    private fun checkUser() {
        val user = authService.getUser()
        if (user != null) {
            binding.tvWelcomeUser.text = getString(R.string.welcome_user_id, user.id)
        } else {
            binding.tvWelcomeUser.text = getString(R.string.welcome)
        }
    }

    private fun signOut() {
        authService.signOut()
        checkUser()
    }
}