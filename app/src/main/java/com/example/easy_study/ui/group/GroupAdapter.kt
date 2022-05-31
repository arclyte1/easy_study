package com.example.easy_study.ui.group

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.easy_study.data.model.Group
import com.example.easy_study.databinding.FragmentGroupItemBinding

class GroupAdapter(
    private val onClickListener: OnClickListener
) : ListAdapter<Group, GroupAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<Group>() {

        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.titleView.text = item.group_title
        holder.subjectView.text = item.subject_title
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: List<Group>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.id }
        newList.reverse()
        super.submitList(newList)
    }

    inner class ViewHolder(binding: FragmentGroupItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val subjectView: TextView = binding.subject
    }

    class OnClickListener(val clickListener: (group: Group) -> Unit) {
        fun onClick(group: Group) = clickListener(group)
    }

}