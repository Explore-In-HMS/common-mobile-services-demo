package huawei.cmsdemo.main.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import huawei.cmsdemo.main.R
import huawei.cmsdemo.main.data.model.ServicesInfo
import huawei.cmsdemo.main.databinding.ItemHomeMenuBinding

class ServicesAdapter(private val serviceList: List<ServicesInfo>, private val onClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServicesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_menu, parent, false)
        return ServicesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ServicesViewHolder,
        position: Int
    ) {
        holder.bind(serviceList[position])
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    inner class ServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(service: ServicesInfo) {
            val binding = ItemHomeMenuBinding.bind(itemView)
            with(binding) {
                tvServiceName.text = service.servicesName
                tvServiceVersion.text = service.version
                Glide.with(itemView.context).load(service.imageUrl).into(ivServiceImage)
                cvService.setOnClickListener {
                    onClick
                }
            }
        }
    }
}