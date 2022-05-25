package com.example.easy_study.ui.student_group_details

import android.animation.LayoutTransition
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
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.FragmentStudentGroupDetailsItemBinding

class StudentGroupDetailsAdapter(
    private val onClickListener: OnClickListener,
    private val role: UserRole.Role
) : ListAdapter<Lesson, StudentGroupDetailsAdapter.ViewHolder>(
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
            FragmentStudentGroupDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.titleView.text = item.title
        holder.subjectView.text = item.date
        holder.detailsLayout.visibility = View.GONE
        holder.rootLayout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        // TODO mark attendance
        holder.itemView.setOnClickListener {
            if (role == UserRole.Role.STUDENT) {
                TransitionManager.beginDelayedTransition(holder.rootLayout, AutoTransition())
                holder.detailsLayout.visibility = if (holder.detailsLayout.visibility == View.GONE) View.VISIBLE else View.GONE
            }
            onClickListener.onClick(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    inner class ViewHolder(binding: FragmentStudentGroupDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.title
        val subjectView: TextView = binding.subject
        val detailsLayout: RelativeLayout = binding.detailsLayout
        val rootLayout: LinearLayout = binding.rootLayout
        val attendance: TextView = binding.attendance
        val mark: TextView = binding.mark
    }

    class OnClickListener(val clickListener: (lesson: Lesson) -> Unit) {
        fun onClick(lesson: Lesson) = clickListener(lesson)
    }

}