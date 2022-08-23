package co.kr.kwon.courierservice.presentation.trackingitems

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.kr.kwon.courierservice.R
import co.kr.kwon.courierservice.data.entity.Level
import co.kr.kwon.courierservice.data.entity.ShippingCompany
import co.kr.kwon.courierservice.data.entity.TrackingInformation
import co.kr.kwon.courierservice.data.entity.TrackingItem
import co.kr.kwon.courierservice.databinding.ItemTrackingBinding
import co.kr.kwon.courierservice.extension.setTextColorRes
import co.kr.kwon.courierservice.extension.toReadableDateString
import java.util.*

class TrackingItemsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Pair<TrackingItem, TrackingInformation>> = emptyList()
    var onClickItemListener: ((TrackingItem, TrackingInformation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTrackingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (item, trackingInformation) = data[position]

        (holder as ViewHolder).bind(item.company, trackingInformation)
    }

    override fun getItemCount() = data.size


    inner class ViewHolder(private val binding: ItemTrackingBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                data.getOrNull(adapterPosition)?.let { (item, information) ->
                    onClickItemListener?.invoke(item, information)
                }
            }
        }

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(company: ShippingCompany, information: TrackingInformation) {
            binding.updatedAtTextView.text =
                Date(information.lastDetail?.time ?: System.currentTimeMillis()).toReadableDateString()

            binding.levelLabelTxt.text = information.level?.label
            when (information.level) {
                Level.COMPLETE -> {
                    binding.levelLabelTxt.setTextColor(R.color.orange)
                    binding.root.alpha = 0.5f
                }
                Level.PREPARE -> {
                    binding.levelLabelTxt.setTextColorRes(R.color.orange)
                    binding.root.alpha = 1f
                }
                else -> {
                    binding.levelLabelTxt.setTextColorRes(R.color.green)
                    binding.root.alpha = 1f
                }
            }

            binding.invoiceTextView.text = information.invoiceNo

            if (information.itemName.isNullOrBlank()) {
                binding.itemNameTxt.text = "이름 없음"
                binding.itemNameTxt.setTextColorRes(R.color.gray)
            } else {
                binding.itemNameTxt.text = information.itemName
                binding.itemNameTxt.setTextColorRes(R.color.black)
            }

            binding.lastStateTxt.text = information.lastDetail?.let { it.kind + " @${it.where}" }

            binding.companyNameTxt.text = company.name
        }
    }
}
