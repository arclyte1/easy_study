package com.example.easy_study.domain.use_case

import com.example.easy_study.common.Resource
import com.example.easy_study.domain.model.Group
import com.example.easy_study.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGroupDetailsUseCase @Inject constructor(
    val repository: GroupRepository
){
    operator fun invoke(groupId: Long): Flow<Resource<Group>> = flow {
        try {
            emit(Resource.Loading())
            val group = repository.getGroupDetails(groupId)
            if (group != null)
                emit(Resource.Success(group))
            else
                emit(Resource.Error("Group not found"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}