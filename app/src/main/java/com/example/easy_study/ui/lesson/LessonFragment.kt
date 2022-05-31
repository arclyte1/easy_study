package com.example.easy_study.ui.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.easy_study.R
import com.example.easy_study.databinding.AddUserDialogBinding
import com.example.easy_study.databinding.ChangeMarkDialogBinding
import com.example.easy_study.databinding.FragmentLessonBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
                            viewModel.setMark(user, mark)
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

        binding.addUser.setOnClickListener {
            val v = LayoutInflater.from(requireContext())
                .inflate(R.layout.add_user_dialog, null, false)
            val vBinding = AddUserDialogBinding.bind(v)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.add_student))
                .setView(v)
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.add)) { dialog, which ->
                    if (!vBinding.email.editText?.text.isNullOrBlank()) {
                        viewModel.addStudent(vBinding.email.editText?.text.toString())
                    }
                }
                .show()
        }

        binding.list.adapter = adapter
        viewModel.getStudentList()
    }
}