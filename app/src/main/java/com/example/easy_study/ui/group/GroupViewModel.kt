package com.example.easy_study.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy_study.R
import com.example.easy_study.data.GroupRepository
import com.example.easy_study.data.Result
import com.example.easy_study.data.model.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel() : ViewModel() {

    private val groupRepository = GroupRepository.instance

    private val _getGroupsResult = MutableLiveData<GetGroupsResult>()
    val getGroupsResult: LiveData<GetGroupsResult> = _getGroupsResult

    private val _gettingGroups = MutableLiveData<Boolean>()
    val gettingGroups: LiveData<Boolean> = _gettingGroups

    fun getGroups() {
        CoroutineScope(Dispatchers.IO).launch {
            _gettingGroups.postValue(true)
            val result = groupRepository.getGroups()
            _gettingGroups.postValue(false)

            if (result is Result.Success) {
                _getGroupsResult.postValue(GetGroupsResult(success = result.data))
            } else {
                _getGroupsResult.postValue(GetGroupsResult(error = R.string.get_groups_failed))
            }
        }
    }

    fun setCurrentGroup(group: Group) {
        groupRepository.currentGroup = group
    }

    fun addGroup(title: String, subject: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = groupRepository.addGroup(title, subject)
            if (result is Result.Success)
                _getGroupsResult.postValue(GetGroupsResult(success = result.data))
        }
    }

}