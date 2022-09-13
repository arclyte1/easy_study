package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.model.Student
import com.example.easy_study.domain.repository.GroupRepository
import com.example.easy_study.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStudentListUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(lessonId: Long): Flow<Resource<List<Student>>> = flow {
        try {
            emit(Resource.Loading())
            val students = repository.getStudentList(lessonId)
            emit(Resource.Success(students))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}