package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.Lesson
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLessonListUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(groupId: Long): Flow<Resource<List<Lesson>>> = flow {
        try {
            emit(Resource.Loading())
            val lessons = repository.getLessonList(groupId)
            emit(Resource.Success(lessons))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}