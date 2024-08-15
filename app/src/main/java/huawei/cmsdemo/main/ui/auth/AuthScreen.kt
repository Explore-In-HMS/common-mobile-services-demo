package huawei.cmsdemo.main.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog

class AuthScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showAlertDialog()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_screen, container, false)
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

}