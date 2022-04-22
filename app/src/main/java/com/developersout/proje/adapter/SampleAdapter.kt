package com.developersout.proje.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.developersout.proje.R
import com.developersout.proje.databinding.ItemImageSliderBinding
import com.developersout.proje.model.BannerUIModel


class SampleAdapter : RecyclerView.Adapter<SampleAdapter.VHolder>() {

    private var items: MutableList<BannerUIModel> = mutableListOf()

    var onItemClick: ((BannerUIModel) -> Unit)? = null

    fun setItem(items: List<BannerUIModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }


    inner class VHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemImageSliderBinding.bind(itemView)



        fun onBind(model: BannerUIModel) {
            binding.imageSlide.load(model.imageUrl)
            binding.textTitle.text = model.title
            binding.textOverview.text = model.overView

            binding.root.setOnClickListener {
                onItemClick?.invoke(model)
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, parent, false)
    )
}