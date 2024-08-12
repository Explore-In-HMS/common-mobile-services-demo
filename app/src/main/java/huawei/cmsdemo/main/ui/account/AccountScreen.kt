package huawei.cmsdemo.main.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog
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
        showAlertDialog()
        initializeAccountService()
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.account_kit) }
        with(servicesInfo) {
            requireContext().showAlertDialog(
                title = servicesName,
                desc = desc,
                versions = version
            )
        }
    }


    private fun initializeAccountService() {
        accountService = AccountService.Factory.create(
            requireContext(),
            SignInParams.Builder()
                .requestEmail()
                .create()
        )

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
                    requireContext().toastShort(
                        msg = getString(
                            R.string.signed_in_as,
                            result.id,
                            result.email,
                            result.givenName
                        )
                    )
                }
            }

            override fun onFailure(error: Exception) {
                requireContext().toastShort(msg = getString(R.string.sign_in_failed, error.message))
            }

            override fun onCancelled() {
                requireContext().toastShort(msg = getString(R.string.sign_in_cancelled))
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
                    result?.let {
                        requireContext().toastShort(
                            msg = getString(
                                R.string.signed_in_as_activity_result,
                                it.email
                            )
                        )
                    }
                }

                override fun onFailure(error: Exception) {
                    requireContext().toastShort(
                        msg = getString(
                            R.string.sign_in_failed_activity_result,
                            error.message
                        )
                    )
                }

                override fun onCancelled() {
                    requireContext().toastShort(msg = getString(R.string.sign_in_cancelled_activity_result))
                }
            })
        }
    }

    private fun signOut() {
        accountService.signOut()
            .addOnSuccessListener {
                requireContext().toastShort(msg = getString(R.string.signed_out))
            }
            .addOnFailureListener {
                requireContext().toastShort(msg = getString(R.string.sign_out_failed, it.message))
            }
            .addOnCanceledListener {
                requireContext().toastShort(msg = getString(R.string.sign_out_cancelled))
            }
    }

    companion object {
        private const val REQUEST_CODE = 1001
    }
}
