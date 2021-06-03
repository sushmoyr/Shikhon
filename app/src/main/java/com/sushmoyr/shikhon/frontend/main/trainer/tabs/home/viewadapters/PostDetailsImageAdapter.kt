package com.sushmoyr.shikhon.frontend.main.trainer.tabs.home.viewadapters

import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sushmoyr.shikhon.databinding.DetailImageLayoutBinding


class PostDetailsImageAdapter: RecyclerView.Adapter<PostDetailsImageAdapter.MyViewHolder>() {

    private var imageList = emptyList<String>()

    class MyViewHolder(val binding: DetailImageLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentItem: String){

            Glide.with(binding.root.context)
                .load(Uri.parse(currentItem))
                .listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load failed on details. Getting Exception")
                            if (e != null) {
                                Log.d("Debug", e.stackTraceToString())
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d("Debug", "Load Success on details")
                            binding.loadingDetails.visibility = View.GONE
                            return false
                        }

                    }
                )
                .into(binding.detailImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DetailImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(imageList[position])

    override fun getItemCount(): Int {
        Log.d("Debug", "Image Size = ${imageList.size}")
        return imageList.count()
    }

    fun setAdapterData(data: List<String>){
        imageList = data
        Log.d("debug", "Details image adapter called")
        notifyDataSetChanged()
    }
}