package com.example.easy_study.ui.student_group

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.easy_study.R
import com.example.easy_study.data.model.Group
import com.example.easy_study.data.model.LoggedInUser
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.FragmentStudentGroupBinding
import com.example.easy_study.databinding.FragmentTeacherGroupBinding

/**
 * A fragment representing a list of Groups for students.
 */
class StudentGroupFragment : Fragment(){

    private lateinit var viewModel: StudentGroupViewModel
    private lateinit var binding: FragmentTeacherGroupBinding
    private lateinit var adapter: StudentGroupAdapter
    private lateinit var role: UserRole.Role

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[StudentGroupViewModel::class.java]
        binding = FragmentTeacherGroupBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString("role")!!)
        if (role != UserRole.Role.TEACHER)
            binding.addGroup.visibility = View.GONE
        adapter = StudentGroupAdapter(
            StudentGroupAdapter.OnClickListener { group ->
                showGroup(group)
            })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAndRemoveTask()
        }

        viewModel.getGroupsResult.observe(viewLifecycleOwner,
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

        viewModel.gettingGroups.observe(viewLifecycleOwner,
            Observer { gettingGroups ->
                gettingGroups ?: return@Observer
                if (gettingGroups)
                    binding.loading.visibility = View.VISIBLE
                else
                    binding.loading.visibility = View.GONE
            })

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        viewModel.getGroups()
    }

    private fun showGroup(group: Group) {
        viewModel.setCurrentGroup(group)
        val bundle = bundleOf("role" to UserRole.toString(role),
            "groupId" to group.id)
        findNavController().navigate(R.id.action_studentGroupFragment_to_studentGroupDetailsFragment, bundle)
    }
}