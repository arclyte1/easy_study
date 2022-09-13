package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.LoggedInUser
import com.example.easy_study.domain.repository.GroupRepository
import com.example.easy_study.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val repository: LoginRepository
){
    operator fun invoke(email: String, password: String): Flow<Resource<LoggedInUser>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(email, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}