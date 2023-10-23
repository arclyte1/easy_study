package com.example.easy_study.domain.use_case

import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.repository.LoginRepository
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    val repository: LoginRepository
){
    operator fun invoke(): LoggedInUser? {
        return repository.getLoggedInUser()
    }
}