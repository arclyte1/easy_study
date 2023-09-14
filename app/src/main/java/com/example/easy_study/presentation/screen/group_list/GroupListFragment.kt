package com.example.easy_study.presentation.screen.group_list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.UserRole
import com.example.easy_study.databinding.AddGroupDialogBinding
import com.example.easy_study.databinding.FragmentGroupListBinding
import com.example.easy_study.presentation.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a list of Groups for students.
 */
@AndroidEntryPoint
class GroupListFragment : Fragment(){

    private val viewModel: GroupListViewModel by viewModels()
    private lateinit var binding: FragmentGroupListBinding
    private lateinit var adapter: GroupListAdapter
    private lateinit var role: UserRole

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupListBinding.bind(view)
        role = UserRole.getValue(requireArguments().getString(Constants.ROLE_FIELD)!!)
        if (role != UserRole.TEACHER)
            binding.addGroup.visibility = View.GONE
        adapter = GroupListAdapter(
            GroupListAdapter.OnClickListener { group ->
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
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
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
        binding.menuButton.setOnClickListener(this::showMenu)

        viewModel.getGroups()
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.group_list_menu, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    requireActivity().finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
            true
        }
    }

    private fun showGroup(group: Group) {
        val bundle = bundleOf(
            Constants.ROLE_FIELD to UserRole.toString(role),
            Constants.GROUP_ID_FIELD to group.id,
            Constants.GROUP_TITLE_FIELD to group.group_title
        )
        findNavController().navigate(R.id.action_groupListFragment_to_lessonListFragment, bundle)
    }
}