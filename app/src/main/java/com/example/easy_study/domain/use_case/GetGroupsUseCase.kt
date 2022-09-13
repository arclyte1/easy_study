package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(): Flow<Resource<List<Group>>> = flow {
        try {
            emit(Resource.Loading())
            val groups = repository.getGroups()
            emit(Resource.Success(groups))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}