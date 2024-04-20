package com.dicoding.sub1fundamental.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.sub1fundamental.data.response.ItemsItem
import com.dicoding.sub1fundamental.databinding.ItemUserBinding


class ItemAdapter : ListAdapter<ItemsItem, ItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int):MyViewHolder{
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val user = getItem(position)

        holder.bind(user)

        holder.itemView.setOnClickListener{
            val urlGambar = user.avatarUrl
            val intent = Intent(holder.binding.root.context, DetailUserActivity::class.java)
            intent.putExtra("imageUrl",urlGambar)
            intent.putExtra("name",user.login)
            holder.binding.root.context.startActivity(intent)
        }
    }
    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: ItemsItem){

            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgItemPhoto)

            binding.tvName.text = "${user.login}"
        }
    }
    companion object{
        val DIFF_CALLBACK = object  :DiffUtil.ItemCallback<ItemsItem>(){
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return  oldItem ==newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}