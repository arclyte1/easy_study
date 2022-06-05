package com.example.easy_study.ui.group_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easy_study.R
import com.example.easy_study.data.model.Lesson
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.AddLessonDialogBinding
import com.example.easy_study.databinding.FragmentGroupDetailsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GroupDetailsFragment : Fragment() {

    private lateinit var viewModel: GroupDetailsViewModel
    private lateinit var binding: FragmentGroupDetailsBinding
    private lateinit var adapter: GroupDetailsAdapter
    private lateinit var role: UserRole.Role

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GroupDetailsViewModel::class.java]
        binding = FragmentGroupDetailsBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString("role")!!)
        if (role != UserRole.Role.TEACHER)
            binding.addLesson.visibility = View.GONE
        adapter = GroupDetailsAdapter(
            requireContext(),
            GroupDetailsAdapter.OnClickListener { lesson ->
                showLesson(lesson)
            }, role)

        viewModel.getLessonsResult.observe(viewLifecycleOwner,
            Observer { getGroupsResult ->
                getGroupsResult ?: return@Observer
                binding.loading.visibility = View.GONE
                getGroupsResult.error?.let {
                    Toast.makeText(context, getString(it), Toast.LENGTH_LONG).show()
                }
                getGroupsResult.success?.let {
                    binding.noLessons.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(it)
                }
            })

        viewModel.gettingLessons.observe(viewLifecycleOwner,
            Observer { gettingGroups ->
                gettingGroups ?: return@Observer
                if (gettingGroups)
                    binding.loading.visibility = View.VISIBLE
                else
                    binding.loading.visibility = View.GONE
            })

        binding.addLesson.setOnClickListener {
            val v = LayoutInflater.from(requireContext())
                .inflate(R.layout.add_lesson_dialog, null, false)
            val vBinding = AddLessonDialogBinding.bind(v)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.add_lesson))
                .setView(v)
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to negative button press
                }
                // TODO: date support
//                .setNeutralButton(resources.getString(R.string.pick_date)) { dialog, which ->
//                    val datePicker = MaterialDatePicker.Builder.datePicker()
//                        .setTitleText("Select date")
//                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//                        .build()
//                    datePicker.addOnPositiveButtonClickListener {
//                        vBinding.date.text = datePicker.headerText
//                    }
//                    datePicker.show(childFragmentManager, "lesson date")
//                }
                .setPositiveButton(resources.getString(R.string.add)) { dialog, which ->
                    if (vBinding.title.editText!!.text.isNullOrBlank()) {
                        Toast.makeText(requireContext(), getString(R.string.fields_cant_be_empty), Toast.LENGTH_LONG).show()
                    } else {
                        val title = vBinding.title.editText!!.text.toString()
                        viewModel.addLesson(title)
                    }
                }
                .show()
        }

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        viewModel.getStudentGroupDetails(requireArguments().getLong("groupId"))
    }

    private fun showLesson(lesson: Lesson) {
        if (role == UserRole.Role.STUDENT) {

        } else {
            viewModel.setCurrentLesson(lesson)
            findNavController().navigate(R.id.action_studentGroupDetailsFragment_to_lessonFragment)
        }
    }

}