package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.Student
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStudentProgressUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(groupId: Long, email: String): Flow<Resource<List<Lesson>>> = flow {
        try {
            emit(Resource.Loading())
            val lessons = repository.getStudentProgress(groupId, email)
            emit(Resource.Success(lessons))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}