package com.example.easy_study.presentation.lesson_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.databinding.AddLessonDialogBinding
import com.example.easy_study.databinding.FragmentLessonListBinding
import com.example.easy_study.presentation.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonListFragment : Fragment() {

    private val viewModel: LessonListViewModel by viewModels()
    private lateinit var binding: FragmentLessonListBinding
    private lateinit var adapter: LessonListAdapter
    private lateinit var role: UserRole.Role
    private var groupId = -1L
    private lateinit var groupTitle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lesson_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLessonListBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString(Constants.ROLE_FIELD)!!)
        groupId = requireArguments().getLong(Constants.GROUP_ID_FIELD)
        Log.e("group id", groupId.toString())
        groupTitle = requireArguments().getString(Constants.GROUP_TITLE_FIELD).toString()
        if (role != UserRole.Role.TEACHER)
            binding.addLesson.visibility = View.GONE
        adapter = LessonListAdapter(
            requireContext(),
            LessonListAdapter.OnClickListener { lesson ->
                showLesson(lesson)
            }, role)

        viewModel.getLessonsResult.observe(viewLifecycleOwner,
            Observer { getGroupsResult ->
                getGroupsResult ?: return@Observer
                binding.loading.visibility = View.GONE
                getGroupsResult.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
                        viewModel.addLesson(groupId, title)
                    }
                }
                .show()
        }

        binding.groupTitle.text = groupTitle
        binding.menuButton.setOnClickListener(this::showMenu)

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        viewModel.getLessonList(groupId)
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.lesson_list_menu, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
                R.id.action_show_group_details -> {
                    val bundle = bundleOf(
                        Constants.ROLE_FIELD to UserRole.toString(role),
                        Constants.GROUP_ID_FIELD to groupId
                    )
                    findNavController().navigate(R.id.action_lessonListFragment_to_groupDetailsFragment, bundle)
                }
            }
            true
        }
    }

    private fun showLesson(lesson: Lesson) {
        if (role != UserRole.Role.STUDENT) {
            val bundle = bundleOf(
                Constants.ROLE_FIELD to UserRole.toString(role),
                Constants.LESSON_ID_FIELD to lesson.id
            )
            findNavController().navigate(R.id.action_lessonListFragment_to_lessonDetailsFragment, bundle)
        }
    }

}