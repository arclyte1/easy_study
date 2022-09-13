package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetAttendanceUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(lessonId: Long, userId: Long, attendance: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            repository.setAttendance(lessonId, userId, attendance)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}