package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SetMarkUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(lessonId: Long, userId: Long, mark: Double?): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            repository.setMark(lessonId, userId, mark)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}