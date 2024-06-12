package huawei.cmsdemo.main.ui.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.hms.lib.commonmobileservices.ads.interstitial.InterstitialAd
import com.hms.lib.commonmobileservices.ads.interstitial.common.InterstitialAdLoadCallback
import com.hms.lib.commonmobileservices.ads.interstitial.implementation.IInterstitialAd
import com.hms.lib.commonmobileservices.ads.rewarded.RewardedAd
import com.hms.lib.commonmobileservices.ads.rewarded.common.IRewardItem
import com.hms.lib.commonmobileservices.ads.rewarded.common.RewardedAdLoadCallback
import com.hms.lib.commonmobileservices.ads.rewarded.common.UserRewardEarnedListener
import com.hms.lib.commonmobileservices.ads.rewarded.implementation.IRewardedAd
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentAdsScreenBinding
import huawei.cmsdemo.main.util.Constants.GMS_AD_ID_INTERSTITIAL
import huawei.cmsdemo.main.util.Constants.GMS_AD_ID_REWARDED
import huawei.cmsdemo.main.util.Constants.HMS_AD_ID_INTERSTITIAL
import huawei.cmsdemo.main.util.Constants.HMS_AD_ID_REWARDED
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
            btnBannerAds.setOnClickListener {
                showBannerAd()
            }

            btnRewardedAds.setOnClickListener {
                showRewardedAd()
            }

            btnInterstitialAds.setOnClickListener {
                showInterstitialAd()
            }
        }
    }

    private fun showBannerAd() {
        val bannerView = binding.hwBannerView
        val adParam = AdParam.Builder().build()
        bannerView.loadAd(adParam)

        val adListener: AdListener = object : AdListener() {
            override fun onAdLoaded() {
                requireContext().toastShort(getString(R.string.banner_ad_loaded))
            }

            override fun onAdFailed(errorCode: Int) {
                requireContext().toastShort(getString(R.string.banner_ad_failed) + errorCode)
            }

            override fun onAdClosed() {
                bannerView.destroy()
                requireContext().toastShort(getString(R.string.banner_ad_closed))
            }
        }

        bannerView.adListener = adListener
    }

    private fun showRewardedAd() {
        showProgress()
        requireContext().toastLong(getString(R.string.rewarded_ad_loading))
        val adRequest = AdManagerAdRequest.Builder().build()
        val adParam = AdParam.Builder().build()

        RewardedAd.load(
            requireContext(),
            HMS_AD_ID_REWARDED,
            GMS_AD_ID_REWARDED,
            object : RewardedAdLoadCallback {
                override fun onAdLoadFailed(adError: String) {
                    hideProgress()
                    requireContext().toastShort(getString(R.string.rewarded_ad_failed) + adError)
                }

                override fun onRewardedAdLoaded(rewardedAd: IRewardedAd) {
                    hideProgress()
                    requireContext().toastShort(getString(R.string.rewarded_ad_loaded))
                    rewardedAd.show(requireActivity(), object : UserRewardEarnedListener {
                        override fun onUserEarnedReward(item: IRewardItem) {
                            requireContext().toastShort(
                                getString(R.string.earned_reward_score) + item.getAmount()
                                    .toString()
                            )
                        }
                    })
                }
            },
            adRequest,
            adParam
        )
    }

    private fun showInterstitialAd() {
        showProgress()
        requireContext().toastLong(getString(R.string.interstitial_ad_loading))
        val adRequest = AdRequest.Builder().build()
        val adParam = AdParam.Builder().build()

        InterstitialAd.load(
            requireContext(),
            HMS_AD_ID_INTERSTITIAL,
            GMS_AD_ID_INTERSTITIAL,
            adRequest,
            adParam,
            object :
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