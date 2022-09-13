package com.example.easy_study.presentation.group_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.databinding.AddUserDialogBinding
import com.example.easy_study.databinding.FragmentGroupDetailsBinding
import com.example.easy_study.databinding.FragmentGroupListBinding
import com.example.easy_study.domain.model.User
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.presentation.group_list.GroupListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class GroupDetailsFragment: Fragment(R.layout.fragment_group_details) {

    private val viewModel: GroupDetailsViewModel by viewModels()
    private lateinit var binding: FragmentGroupDetailsBinding
    private lateinit var studentListAdapter: GroupDetailsAdapter
    private lateinit var teacherListAdapter: GroupDetailsAdapter
    private var groupId = -1L
    private lateinit var role: UserRole.Role

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupDetailsBinding.bind(view)
        groupId = requireArguments().getLong(Constants.GROUP_ID_FIELD)
        role = UserRole.getValue(requireArguments().getString(Constants.ROLE_FIELD)!!)

        binding.addStudent.visibility = if (role == UserRole.Role.TEACHER) View.VISIBLE else View.GONE
        binding.addTeacher.visibility = if (role == UserRole.Role.TEACHER) View.VISIBLE else View.GONE

        studentListAdapter = GroupDetailsAdapter(
            GroupDetailsAdapter.OnClickListener {showStudent(it)}
        )
        binding.studentList.adapter = studentListAdapter

        teacherListAdapter = GroupDetailsAdapter()
        binding.teacherList.adapter = teacherListAdapter

        lifecycleScope.launchWhenStarted {
            viewModel.getGroupDetailsState.collect {
                binding.loading.visibility = if (it.isLoading) View.VISIBLE else View.GONE

                if (it.group != null) {
                    binding.groupTitle.text = it.group.group_title
                    binding.subjectTitle.text = it.group.subject_title
                    val studentsCount = "(${it.group.students.size})"
                    val teachersCount = "(${it.group.teachers.size})"
                    binding.studentsCount.text = studentsCount
                    binding.teachersCount.text = teachersCount
                    studentListAdapter.submitList(it.group.students)
                    teacherListAdapter.submitList(it.group.teachers)
                }

                if (it.error.isNotBlank())
                    Snackbar.make(
                        binding.root,
                        it.error,
                        Snackbar.LENGTH_LONG
                    ).show()
            }
        }

        binding.addStudent.setOnClickListener {
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
                        viewModel.addStudent(groupId, vBinding.email.editText?.text.toString())
                    }
                }
                .show()
        }

        binding.addTeacher.setOnClickListener {
            val v = LayoutInflater.from(requireContext())
                .inflate(R.layout.add_user_dialog, null, false)
            val vBinding = AddUserDialogBinding.bind(v)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.add_teacher))
                .setView(v)
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.add)) { dialog, which ->
                    if (!vBinding.email.editText?.text.isNullOrBlank()) {
                        viewModel.addTeacher(groupId, vBinding.email.editText?.text.toString())
                    }
                }
                .show()
        }

        viewModel.getGroupDetails(groupId)
    }

    private fun showStudent(user: User) {
        if (role == UserRole.Role.TEACHER) {
            val bundle = bundleOf(
                Constants.GROUP_ID_FIELD to groupId,
                Constants.EMAIL to user.email,
                Constants.NAME to user.name
            )
            findNavController().navigate(
                R.id.action_groupDetailsFragment_to_studentDetailsFragment,
                bundle
            )
        }
    }
}