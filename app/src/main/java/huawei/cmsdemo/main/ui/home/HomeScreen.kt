package huawei.cmsdemo.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import huawei.cmsdemo.main.data.model.ServicesInfo
import huawei.cmsdemo.main.databinding.FragmentHomeScreenBinding
import huawei.cmsdemo.main.ui.home.adapter.ServicesAdapter

class HomeScreen : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater)
        initUI ()
        return binding.root
    }

    private fun initUI () {
        val list = listOf (
            ServicesInfo("Push Kit", "1.3.5", "data:image/png;base64,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"),
            ServicesInfo("Map Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/1865/1865153.png"),
            ServicesInfo("Location Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/684/684809.png"),
            ServicesInfo("Analytics Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/11738/11738555.png"),
            ServicesInfo("Account Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/709/709579.png"),
            ServicesInfo("Auth Service", "2.4.5","https://cdn-icons-png.flaticon.com/128/5048/5048687.png"),
            ServicesInfo("Ads Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/6317/6317510.png"),
            ServicesInfo("Image Classification", "2.4.5", "https://cdn-icons-png.flaticon.com/128/3342/3342137.png"),
            ServicesInfo("Safety Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/1161/1161490.png"),
            ServicesInfo("Crash", "2.4.5", "https://cdn-icons-png.flaticon.com/128/8603/8603355.png"),
            ServicesInfo("Identity Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/837/837834.png"),
            ServicesInfo("Remote Configuration", "2.4.5", "https://cdn-icons-png.flaticon.com/128/3736/3736109.png"),
            ServicesInfo("Credit Card Scanner", "2.4.5", "https://cdn-icons-png.flaticon.com/128/657/657076.png"),
            ServicesInfo("Awareness Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/925/925434.png"),
            ServicesInfo("Scan Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/7463/7463734.png"),
            ServicesInfo("Translate Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/4534/4534766.png"),
            ServicesInfo("Speech To Text", "2.4.5", "https://cdn-icons-png.flaticon.com/128/13063/13063588.png"),
            ServicesInfo("Text To Speech", "2.4.5", "https://cdn-icons-png.flaticon.com/128/13063/13063588.png"),
            ServicesInfo("Object Detection", "2.4.5", "https://cdn-icons-png.flaticon.com/128/8654/8654854.png"),
            ServicesInfo("Text Recognition", "2.4.5", "https://cdn-icons-png.flaticon.com/128/2476/2476994.png"),
            ServicesInfo("Face Detection", "2.4.5", "https://cdn-icons-png.flaticon.com/128/5107/5107483.png"),
            ServicesInfo("Language Detection", "2.4.5", "https://cdn-icons-png.flaticon.com/128/2115/2115307.png"),
            ServicesInfo("Site Kit", "2.4.5", "https://cdn-icons-png.flaticon.com/128/10398/10398263.png"),
        )
        binding.rvServices.adapter = ServicesAdapter(list) {}
    }
}