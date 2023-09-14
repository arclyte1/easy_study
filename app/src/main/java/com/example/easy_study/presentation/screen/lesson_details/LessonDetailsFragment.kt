package com.example.easy_study.presentation.screen.lesson_details

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.databinding.AddUserDialogBinding
import com.example.easy_study.databinding.ChangeMarkDialogBinding
import com.example.easy_study.databinding.FragmentLessonDetailsBinding
import com.example.easy_study.domain.model.Student
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLessonDetailsBinding
    private val viewModel: LessonDetailsViewModel by viewModels()
    private lateinit var adapter: LessonDetailsAdapter
    private lateinit var currentUser: Student

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lesson_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLessonDetailsBinding.bind(view)
        val lessonId = requireArguments().getLong(Constants.LESSON_ID_FIELD)
        adapter = LessonDetailsAdapter(
            requireContext(),
            LessonDetailsAdapter.OnClickListener(
                // attendance click listener
                { user ->
                    adapter.setAttendance(user, !user.attendance)
                    viewModel.setAttendance(lessonId, user, user.attendance)
                },
                // mark click listener
                { user ->
                    val v = LayoutInflater.from(requireContext())
                        .inflate(R.layout.change_mark_dialog, null, false)
                    val vBinding = ChangeMarkDialogBinding.bind(v)
                    if (user.mark != null)
                        vBinding.mark.editText!!.setText(user.mark.toString())
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.change_mark))
                        .setView(v)
                        .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                            // Respond to negative button press
                        }
                        .setPositiveButton(resources.getString(R.string.apply)) { dialog, which ->
                            val mark = if (vBinding.mark.editText?.text.isNullOrBlank()) null
                            else vBinding.mark.editText?.text.toString().toDouble()
                            adapter.setMark(user, mark)
                            viewModel.setMark(lessonId, user, mark)
                        }
                        .show()
//                    val dialog = MarkDialogFragment(
//                        MarkDialogFragment.OnClickListener { mark ->
//                            adapter.setMark(user, mark)
//                            viewModel.setMark(user, mark)
//                        }
//                    )
//                    dialog.arguments = bundleOf("mark" to (user.mark ?: -1.0))
//                    dialog.show(childFragmentManager, "mark dialog")
                }
            )
        )

        viewModel.studentList.observe(viewLifecycleOwner,
            Observer {
                binding.noStudents.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                adapter.submitList(it)
            }
        )

        binding.list.adapter = adapter
        viewModel.getStudentList(lessonId)
    }

}