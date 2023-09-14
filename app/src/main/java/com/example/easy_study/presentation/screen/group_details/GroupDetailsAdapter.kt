package com.example.easy_study.presentation.screen.group_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easy_study.R
import com.example.easy_study.databinding.FragmentGroupDetailsItemBinding
import com.example.easy_study.databinding.FragmentLessonDetailsItemBinding
import com.example.easy_study.domain.model.User

class GroupDetailsAdapter(
    private val onClickListener: OnClickListener? = null
) : ListAdapter<User, GroupDetailsAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupDetailsAdapter.ViewHolder {

        return ViewHolder(
            FragmentGroupDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: GroupDetailsAdapter.ViewHolder, position: Int) {
        val item = currentList[position]
        holder.name.text = item.name
        holder.name.setOnClickListener {
            onClickListener?.clickListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: List<User>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.name }
        super.submitList(newList)
    }

    inner class ViewHolder(binding: FragmentGroupDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.username
    }

    class OnClickListener(val clickListener: (user: User) -> Unit) {
        fun onClickListener(user: User) = clickListener(user)
    }

}