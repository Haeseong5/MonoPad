package com.project.monopad.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.monopad.databinding.CasterViewBinding
import com.project.monopad.model.network.response.MovieCastResponse
import com.project.monopad.util.BaseUtil

class CasterAdapter : RecyclerView.Adapter<CasterAdapter.ViewHolder>() {


    private var casterList = ArrayList<MovieCastResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CasterAdapter.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: CasterViewBinding = CasterViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CasterAdapter.ViewHolder, position: Int) {
        holder.bindView(casterList[position])
        holder.onClick(casterList[position])

    }

    override fun getItemCount(): Int {
        return casterList.size
    }

    fun setList(list : List<MovieCastResponse>){
        this.casterList = list as ArrayList<MovieCastResponse>
    }

    inner class ViewHolder(private val binding: CasterViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(cast: MovieCastResponse){
                Glide.with(binding.ivDetailCaster.context)
                    .load(BaseUtil.IMAGE_URL+cast.profile_path)
                    .fitCenter()
                    .into(binding.ivDetailCaster)
        }

        fun onClick(cast: MovieCastResponse){
            binding.ivDetailCaster.setOnClickListener{
                // it.context.startActivity(Intent(it.context, DetailActivity::class.java)) : go person detail
            }
        }

    }
}