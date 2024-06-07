package huawei.cmsdemo.main.ui.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.hms.lib.commonmobileservices.ads.interstitial.InterstitialAd
import com.hms.lib.commonmobileservices.ads.interstitial.common.InterstitialAdLoadCallback
import com.hms.lib.commonmobileservices.ads.interstitial.implementation.IInterstitialAd
import com.huawei.hms.ads.AdParam
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentAdsScreenBinding
import huawei.cmsdemo.main.util.Constants.GMS_AD_ID
import huawei.cmsdemo.main.util.Constants.HMS_AD_ID
import huawei.cmsdemo.main.util.toastLong
import huawei.cmsdemo.main.util.toastShort


class AdsScreen : Fragment() {
    private lateinit var binding: FragmentAdsScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdsScreenBinding.inflate(inflater)
        initUI()
        return binding.root
    }

    private fun initUI() {
        with(binding) {
            btnInterstitialAds.setOnClickListener {
                showProgress()
                requireContext().toastLong(getString(R.string.interstitial_ad_loading))
                val adRequest = AdRequest.Builder().build()
                val adParam = AdParam.Builder().build()

                InterstitialAd.load(requireContext(), HMS_AD_ID, GMS_AD_ID, adRequest, adParam, object :
                    InterstitialAdLoadCallback {
                    override fun onAdLoadFailed(adError: String) {
                        hideProgress()
                        requireContext().toastShort(getString(R.string.interstitial_ad_failed) + adError)
                    }

                    override fun onInterstitialAdLoaded(interstitialAd: IInterstitialAd) {
                        hideProgress()
                        requireContext().toastShort(getString(R.string.interstitial_ad_loaded))
                        interstitialAd.show(requireActivity())
                    }
                })
            }
        }    }


    private fun showProgress() {
        with(binding) {
            progressBar.isVisible = true
            clMainView.isVisible = false
        }
    }
    private fun hideProgress() {
        with(binding) {
            progressBar.isVisible = false
            clMainView.isVisible = true
        }
    }
}