package com.example.easy_study.common

import com.example.easy_study.R

sealed class ValidatingResult(open val errorStringResId: Int) {

    object Valid : ValidatingResult(R.string.empty_string)
    data class Invalid(override val errorStringResId: Int) : ValidatingResult(errorStringResId)
}
