package com.example.easy_study.ui.student_group_details

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
import com.example.easy_study.databinding.FragmentStudentGroupDetailsBinding

class StudentGroupDetailsFragment : Fragment() {

    private lateinit var viewModel: StudentGroupDetailsViewModel
    private lateinit var binding: FragmentStudentGroupDetailsBinding
    private lateinit var adapter: StudentGroupDetailsAdapter
    private lateinit var role: UserRole.Role

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_group_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StudentGroupDetailsViewModel::class.java]
        binding = FragmentStudentGroupDetailsBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString("role")!!)
        if (role != UserRole.Role.TEACHER)
            binding.addLesson.visibility = View.GONE
        adapter = StudentGroupDetailsAdapter(
            StudentGroupDetailsAdapter.OnClickListener { lesson ->
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
                    adapter.submitList(it)
                    Log.d("Groups", it.toString())
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