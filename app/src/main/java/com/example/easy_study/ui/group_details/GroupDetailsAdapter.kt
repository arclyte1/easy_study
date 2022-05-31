package com.example.easy_study.ui.group_details

import android.animation.LayoutTransition
import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easy_study.R
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.FragmentGroupDetailsItemBinding

class GroupDetailsAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener,
    private val role: UserRole.Role
) : ListAdapter<Lesson, GroupDetailsAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<Lesson>() {

        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentGroupDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.titleView.text = item.title
        holder.detailsLayout.visibility = View.GONE
        holder.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        // TODO date
        holder.mark.text = item.mark.toString()
        holder.attendance.text = if (item.attendance == true)
            context.getString(R.string.attendance_yes) else context.getString(R.string.attendance_no)
        holder.itemView.setOnClickListener {
            if (role == UserRole.Role.STUDENT) {
                TransitionManager.beginDelayedTransition(holder.rootLayout, AutoTransition())
                holder.detailsLayout.visibility = if (holder.detailsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
            }
            onClickListener.onClick(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: List<Lesson>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.id }
        newList.reverse()
        super.submitList(newList)
    }

    inner class ViewHolder(binding: FragmentGroupDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val detailsLayout: RelativeLayout = binding.detailsLayout
        val rootLayout: LinearLayout = binding.rootLayout
        val attendance: TextView = binding.attendance
        val mark: TextView = binding.mark
    }

    class OnClickListener(val clickListener: (lesson: Lesson) -> Unit) {
        fun onClick(lesson: Lesson) = clickListener(lesson)
    }

}