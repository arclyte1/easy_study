package com.example.easy_study.presentation.student_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easy_study.R
import com.example.easy_study.databinding.FragmentStudentDetailsItemBinding
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.User

class StudentDetailsAdapter(
    private val context: Context
) : ListAdapter<Lesson, StudentDetailsAdapter.ViewHolder>(
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
            FragmentStudentDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.title.text = item.title
        holder.mark.text = if (item.mark != null) item.mark.toString() else context.getString(R.string.attendance_no)
        holder.attendance.text = if (item.attendance == true)
            context.getString(R.string.attendance_yes) else context.getString(R.string.attendance_no)
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: List<Lesson>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.id }
        newList.reverse()
        super.submitList(newList)
    }

    inner class ViewHolder(binding: FragmentStudentDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.name
        val mark = binding.mark
        val attendance = binding.attendance
    }

}