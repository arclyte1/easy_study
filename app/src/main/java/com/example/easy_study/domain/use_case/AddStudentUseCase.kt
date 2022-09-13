package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddStudentUseCase @Inject constructor(
    val repository: GroupRepository
) {
    operator fun invoke(groupId: Long, email: String): Flow<Resource<Group>> = flow {
        try {
            emit(Resource.Loading())
            val group = repository.addStudent(groupId, email)
            emit(Resource.Success(group))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}