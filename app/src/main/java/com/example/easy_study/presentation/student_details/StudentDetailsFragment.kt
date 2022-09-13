package com.example.easy_study.presentation.student_details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.easy_study.R
import com.example.easy_study.common.Constants
import com.example.easy_study.databinding.FragmentStudentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentDetailsFragment: Fragment(R.layout.fragment_student_details) {

    private val viewModel: StudentDetailsViewModel by viewModels()
    private lateinit var binding: FragmentStudentDetailsBinding
    private lateinit var adapter: StudentDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStudentDetailsBinding.bind(view)
        adapter = StudentDetailsAdapter(requireContext())
        binding.list.adapter = adapter
        val studentName = requireArguments().getString(Constants.NAME)
        binding.studentName.text = studentName

        lifecycleScope.launchWhenStarted {
            viewModel.getStudentProgressState.collect {
                binding.loading.visibility = if (it.isLoading) View.VISIBLE else View.GONE

                if (it.lessons != null) {
                    adapter.submitList(it.lessons)
                }

                if (it.error.isNotBlank()) {
                    Log.e("123", it.error)
                    Snackbar.make(
                        binding.root,
                        it.error,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        val groupId = requireArguments().getLong(Constants.GROUP_ID_FIELD)
        val email = requireArguments().getString(Constants.EMAIL)!!
        viewModel.getStudentDetails(groupId, email)
    }
}