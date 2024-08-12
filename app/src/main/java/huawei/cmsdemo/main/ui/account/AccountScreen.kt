package huawei.cmsdemo.main.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hms.lib.commonmobileservices.account.AccountService
import com.hms.lib.commonmobileservices.account.SignInParams
import com.hms.lib.commonmobileservices.account.SignInUser
import com.hms.lib.commonmobileservices.core.ResultCallback
import huawei.cmsdemo.main.databinding.FragmentAccountScreenBinding
import huawei.cmsdemo.main.util.toastShort

class AccountScreen : Fragment() {
    private lateinit var binding: FragmentAccountScreenBinding
    private lateinit var accountService: AccountService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountScreenBinding.inflate(inflater, container, false)
        initializeAccountService()
        return binding.root
    }

    private fun initializeAccountService() {
        // Initialize AccountService
        accountService = AccountService.Factory.create(
            requireContext(),
            SignInParams.Builder()
                .requestEmail()
                .create()
        )


        // Setup sign-in button
        binding.huaweiSignInButton.setSignInButtonClickListener {
            signIn()
        }

        binding.signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signIn() {
        accountService.silentSignIn(object : ResultCallback<SignInUser> {
            override fun onSuccess(result: SignInUser?) {
                if (result != null) {
                    requireContext().toastShort(msg = "Signed in as HUAWEI: ${result.id} ${result.email} ${result.givenName}}")

                }
            }

            override fun onFailure(error: Exception) {
                requireContext().toastShort(msg = "Sign-in failed HUAWEI: ${error.message}")
            }

            override fun onCancelled() {
                requireContext().toastShort(msg = "Cancelled")
            }
        })

        accountService.getSignInIntent { intent ->
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            accountService.onSignInActivityResult(data, object : ResultCallback<SignInUser> {
                override fun onSuccess(result: SignInUser?) {
                    // Handle success
                    result?.let {
                        requireContext().toastShort(msg = "Signed in as activity result: ${it.email}")
                    }
                }

                override fun onFailure(error: Exception) {
                    // Handle failure
                    requireContext().toastShort(msg = "Sign-in failed activity result: ${error.message}")
                }

                override fun onCancelled() {
                    // Handle cancellation
                    requireContext().toastShort(msg = "Sign-in cancelled activity result")
                }
            })
        }
    }

    private fun signOut() {
        accountService.signOut()
            .addOnSuccessListener {
                // Handle successful sign-out
                requireContext().toastShort(msg = "Signed out")
            }
            .addOnFailureListener {
                // Handle sign-out failure
                requireContext().toastShort(msg =  "Sign-out failed: ${it.message}")
            }
            .addOnCanceledListener {
                // Handle sign-out cancellation
                requireContext().toastShort(msg = "Sign-out cancelled")
            }
    }

    companion object {
        private const val REQUEST_CODE = 1001
    }
}
