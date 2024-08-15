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
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentAccountScreenBinding
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog

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

        binding.buttonSignIn.setSignInButtonClickListener {
            signIn()
        }

        binding.buttonGetLastSignedAccount.setOnClickListener {
            getLastSignedAccount()
        }

        binding.buttonSignOut.setOnClickListener {
            signOut()
        }
    }

    private fun signIn() {
        accountService.getSignInIntent { intent ->
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun getLastSignedAccount() {
        accountService.silentSignIn(object : ResultCallback<SignInUser> {
            override fun onSuccess(result: SignInUser?) {
                if (result != null) {
                    val userInfoText =
                        getString(
                            R.string.id_email_name,
                            result.id,
                            result.email,
                            result.givenName
                        ).trimIndent()
                    binding.textViewAccountInfo.text = userInfoText
                }
            }

            override fun onFailure(error: Exception) {
                binding.textViewAccountInfo.text = getString(R.string.please_sign_in_first)
            }

            override fun onCancelled() {
                binding.textViewAccountInfo.text = getString(R.string.sign_in_cancelled)
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            accountService.onSignInActivityResult(data, object : ResultCallback<SignInUser> {
                override fun onSuccess(result: SignInUser?) {
                    result?.let {
                        val userInfoText =
                            getString(
                                R.string.id_email_name,
                                result.id,
                                result.email,
                                result.givenName
                            ).trimIndent()
                        binding.textViewAccountInfo.text = userInfoText
                    }
                }

                override fun onFailure(error: Exception) {
                    binding.textViewAccountInfo.text = getString(
                        R.string.sign_in_failed_activity_result,
                        error.message
                    )
                }

                override fun onCancelled() {
                    binding.textViewAccountInfo.text =
                        getString(R.string.sign_in_cancelled_activity_result)
                }
            })
        }
    }

    private fun signOut() {
        accountService.signOut()
            .addOnSuccessListener {
                binding.textViewAccountInfo.text = getString(R.string.signed_out)
            }
            .addOnFailureListener {
                binding.textViewAccountInfo.text = getString(R.string.sign_out_failed, it.message)
            }
            .addOnCanceledListener {
                binding.textViewAccountInfo.text = getString(R.string.sign_out_cancelled)
            }
    }

    companion object {
        private const val REQUEST_CODE = 1001
    }
}
