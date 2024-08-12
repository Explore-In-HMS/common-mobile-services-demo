package huawei.cmsdemo.main.ui.ads

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.hms.lib.commonmobileservices.ads.banner.common.BannerAdLoadCallback
import com.hms.lib.commonmobileservices.ads.banner.implementation.IBannerAd
import com.hms.lib.commonmobileservices.ads.interstitial.InterstitialAd
import com.hms.lib.commonmobileservices.ads.interstitial.common.InterstitialAdLoadCallback
import com.hms.lib.commonmobileservices.ads.interstitial.implementation.IInterstitialAd
import com.hms.lib.commonmobileservices.ads.rewarded.RewardedAd
import com.hms.lib.commonmobileservices.ads.rewarded.common.IRewardItem
import com.hms.lib.commonmobileservices.ads.rewarded.common.RewardedAdLoadCallback
import com.hms.lib.commonmobileservices.ads.rewarded.common.UserRewardEarnedListener
import com.hms.lib.commonmobileservices.ads.rewarded.implementation.IRewardedAd
import com.hms.lib.commonmobileservices.ads.splash.common.SplashAdLoadCallback
import com.hms.lib.commonmobileservices.ads.splash.implementation.ISplashAd
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.databinding.FragmentAdsScreenBinding
import huawei.cmsdemo.main.util.Constants.GMS_AD_ID_INTERSTITIAL
import huawei.cmsdemo.main.util.Constants.GMS_AD_ID_REWARDED
import huawei.cmsdemo.main.util.Constants.HMS_AD_ID_INTERSTITIAL
import huawei.cmsdemo.main.util.Constants.HMS_AD_ID_REWARDED
import huawei.cmsdemo.main.util.Util
import huawei.cmsdemo.main.util.showAlertDialog
import huawei.cmsdemo.main.util.toastShort

class AdsScreen : Fragment() {
    private lateinit var binding: FragmentAdsScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdsScreenBinding.inflate(inflater)
        showAlertDialog()

        initUI()
        return binding.root
    }

    private fun showAlertDialog() {
        val servicesInfo =
            Util.servicesInfoList.first { it.servicesName == getString(R.string.ads_kit) }
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
            btnBannerAds.setOnClickListener {
                showBannerAd()
            }

            btnRewardedAds.setOnClickListener {
                showRewardedAd()
            }

            btnInterstitialAds.setOnClickListener {
                showInterstitialAd()
            }

            btnSplashAds.setOnClickListener {
                showSplashAd()
            }
        }
    }

    private fun showSplashAd() {
        showProgress()
        requireContext().toastShort(msg = getString(R.string.splash_ad_loading))

        val splashAdView = binding.hwSplashView
        splashAdView.load(callback = object : SplashAdLoadCallback {
            override fun onAdLoadFailed(adError: String) {
                binding.progressBar.visibility = View.GONE
                requireContext().toastShort(getString(R.string.splash_ad_failed) + adError)
            }

            override fun onSplashAdLoaded(splashAd: ISplashAd) {
                binding.progressBar.visibility = View.GONE
                requireContext().toastShort(getString(R.string.splash_ad_loaded))
                splashAd.show(requireActivity())
                simulateAdDismissal()
            }
        })
    }

    private fun showBannerAd() {
        val bannerView = binding.cvBannerAd
        requireContext().toastShort(getString(R.string.banner_ad_loading))

        bannerView.initialize(
            object : BannerAdLoadCallback {
                override fun onAdLoadFailed(adError: String) {
                    requireContext().toastShort(getString(R.string.banner_ad_failed))
                }

                override fun onBannerAdLoaded(bannerAd: IBannerAd) {
                    requireContext().toastShort(getString(R.string.banner_ad_loaded))
                }
            }
        )
    }

    private fun showRewardedAd() {
        showProgress()
        requireContext().toastShort(getString(R.string.rewarded_ad_loading))

        RewardedAd.load(
            context = requireContext(),
            hmsAdUnitId = HMS_AD_ID_REWARDED,
            gmsAdUnitId = GMS_AD_ID_REWARDED,
            callback = object : RewardedAdLoadCallback {
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
            }
        )
    }

    private fun showInterstitialAd() {
        showProgress()
        requireContext().toastShort(msg = getString(R.string.interstitial_ad_loading))

        InterstitialAd.load(
            context = requireContext(),
            hmsAdUnitId = HMS_AD_ID_INTERSTITIAL,
            gmsAdUnitId = GMS_AD_ID_INTERSTITIAL,
            callback = object : InterstitialAdLoadCallback {
                override fun onAdLoadFailed(adError: String) {
                    hideProgress()
                    requireContext().toastShort(msg = getString(R.string.interstitial_ad_failed) + adError)
                }

                override fun onInterstitialAdLoaded(interstitialAd: IInterstitialAd) {
                    hideProgress()
                    requireContext().toastShort(msg = getString(R.string.interstitial_ad_loaded))
                    interstitialAd.show(requireActivity())
                }
            })
    }

    private fun simulateAdDismissal() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.clMainView.visibility = View.VISIBLE // Restore UI visibility after ad dismissal
        }, 3000) // 3000 milliseconds = 3 seconds
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