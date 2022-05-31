package com.example.easy_study.ui.lesson

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.easy_study.R
import com.example.easy_study.databinding.FragmentLessonItemBinding

class LessonAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener
) : ListAdapter<LessonUser, LessonAdapter.ViewHolder>(
    object: DiffUtil.ItemCallback<LessonUser>() {

        override fun areItemsTheSame(oldItem: LessonUser, newItem: LessonUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LessonUser, newItem: LessonUser): Boolean {
            return oldItem == newItem
        }

    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLessonItemBinding.inflate(
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

    override fun submitList(list: List<LessonUser>?) {
        val newList = list!!.toMutableList()
        newList.sortBy { it.name }
        super.submitList(newList)
    }

    fun setMark(user: LessonUser, mark: Double?) {
        val item = currentList.find {
            it.id == user.id
        }
        item?.mark = mark
        notifyItemChanged(currentList.indexOf(item))
    }

    fun setAttendance(user: LessonUser, attendance: Boolean) {
        val item = currentList.find {
            it.id == user.id
        }
        item?.attendance = attendance
        notifyItemChanged(currentList.indexOf(item))
    }

    inner class ViewHolder(binding: FragmentLessonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.name
        val attendanceView: TextView = binding.attendance
        val markView: TextView = binding.mark
    }

    class OnClickListener(val attendanceClickListener: (user: LessonUser) -> Unit,
                          val markClickListener: (user: LessonUser) -> Unit) {
        fun onAttendanceClickListener(user: LessonUser) = attendanceClickListener(user)
        fun onMarkClickListener(user: LessonUser) = markClickListener(user)
    }

}