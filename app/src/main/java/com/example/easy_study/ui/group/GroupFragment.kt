package com.example.easy_study.ui.group

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
import com.example.easy_study.data.model.UserRole
import com.example.easy_study.databinding.AddGroupDialogBinding
import com.example.easy_study.databinding.FragmentGroupBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A fragment representing a list of Groups for students.
 */
class GroupFragment : Fragment(){

    private lateinit var viewModel: GroupViewModel
    private lateinit var binding: FragmentGroupBinding
    private lateinit var adapter: GroupAdapter
    private lateinit var role: UserRole.Role

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GroupViewModel::class.java]
        binding = FragmentGroupBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString("role")!!)
        if (role != UserRole.Role.TEACHER)
            binding.addGroup.visibility = View.GONE
        adapter = GroupAdapter(
            GroupAdapter.OnClickListener { group ->
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
                    binding.noGroups.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(it)
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

        binding.addGroup.setOnClickListener {
            val v = LayoutInflater.from(requireContext())
                .inflate(R.layout.add_group_dialog, null, false)
            val vBinding = AddGroupDialogBinding.bind(v)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.add_group))
                .setView(v)
                .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.add)) { dialog, which ->
                    if (vBinding.subject.editText!!.text.isNullOrBlank() ||
                            vBinding.title.editText!!.text.isNullOrBlank()) {
                        Toast.makeText(requireContext(), getString(R.string.fields_cant_be_empty), Toast.LENGTH_LONG).show()
                    } else {
                        val title = vBinding.title.editText!!.text.toString()
                        val subject = vBinding.subject.editText!!.text.toString()
                        viewModel.addGroup(title, subject)
                    }
                }
                .show()
        }

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