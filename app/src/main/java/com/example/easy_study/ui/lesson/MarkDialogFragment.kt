package com.example.easy_study.ui.lesson

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.easy_study.R
import com.example.easy_study.databinding.DialogFragmentMarkBinding

class MarkDialogFragment(private val onClickListener: OnClickListener) : DialogFragment(R.layout.dialog_fragment_mark) {

    private lateinit var binding: DialogFragmentMarkBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogFragmentMarkBinding.bind(view)

        val mark = requireArguments().getDouble("mark")
        if (mark != -1.0)
            binding.mark.setText(requireArguments().getDouble("mark").toString())

        binding.apply.setOnClickListener {
            val newMark = if (binding.mark.text.isNullOrBlank()) null
            else binding.mark.text.toString().toDouble()

            if (newMark != mark)
                onClickListener.applyClickListener(newMark)
            dialog?.dismiss()
        }

    }

    class OnClickListener(val applyClickListener: (mark: Double?) -> Unit) {
        fun onApplyClickListener(mark: Double?) = applyClickListener(mark)
    }

}