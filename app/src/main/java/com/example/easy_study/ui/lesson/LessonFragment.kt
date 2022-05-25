package com.example.easy_study.ui.lesson

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.easy_study.R
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.databinding.FragmentLessonBinding

class LessonFragment : Fragment() {

    private lateinit var binding: FragmentLessonBinding
    private lateinit var viewModel: LessonViewModel
    private lateinit var adapter: LessonAdapter
    private lateinit var currentUser: LessonUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLessonBinding.bind(view)
        viewModel = ViewModelProvider(this)[LessonViewModel::class.java]
        adapter = LessonAdapter(
            requireContext(),
            LessonAdapter.OnClickListener(
                // attendance click listener
                { user ->
                    adapter.setAttendance(user, !user.attendance)
                    viewModel.setAttendance(user, user.attendance)
                },
                // mark click listener
                { user ->
                    val dialog = MarkDialogFragment(
                        MarkDialogFragment.OnClickListener { mark ->
                            adapter.setMark(user, mark)
                            viewModel.setMark(user, mark)
                        }
                    )
                    dialog.arguments = bundleOf("mark" to (user.mark ?: -1.0))
                    dialog.show(childFragmentManager, "mark dialog")
                }
            )
        )

        viewModel.studentList.observe(viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        binding.list.adapter = adapter
        viewModel.getStudentList()
    }
}