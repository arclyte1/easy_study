package com.example.easy_study.presentation.lesson_details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easy_study.R
import com.example.easy_study.databinding.FragmentLessonDetailsItemBinding
import com.example.easy_study.domain.model.Student

class LessonDetailsAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : ListAdapter<Student, LessonDetailsAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<Student>() {

        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLessonDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        holder.nameView.text = item.name
        holder.attendanceView.text = if (item.attendance)
            context.getString(R.string.attendance_yes) else context.getString(R.string.attendance_no)
        holder.attendanceView.setOnClickListener {
            onClickListener.attendanceClickListener(item)
        }
        holder.markView.text = (item.mark ?: "—").toString()
        holder.markView.setOnClickListener {
            onClickListener.markClickListener(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun submitList(list: List<Student>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.name }
        super.submitList(newList)
    }

    fun setMark(user: Student, mark: Double?) {
        val item = currentList.find {
            it.id == user.id
        }
        item?.mark = mark
        notifyItemChanged(currentList.indexOf(item))
    }

    fun setAttendance(user: Student, attendance: Boolean) {
        val item = currentList.find {
            it.id == user.id
        }
        item?.attendance = attendance
        notifyItemChanged(currentList.indexOf(item))
    }

    inner class ViewHolder(binding: FragmentLessonDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.name
        val attendanceView: TextView = binding.attendance
        val markView: TextView = binding.mark
    }

    class OnClickListener(val attendanceClickListener: (user: Student) -> Unit,
                          val markClickListener: (user: Student) -> Unit) {
        fun onAttendanceClickListener(user: Student) = attendanceClickListener(user)
        fun onMarkClickListener(user: Student) = markClickListener(user)
    }

}