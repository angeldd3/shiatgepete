package com.lasec.monitoreoapp.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.model.TaskFullDetail
import com.lasec.monitoreoapp.domain.usecase.tasks.GetTaskWithDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTaskWithDetailsUseCase: GetTaskWithDetailsUseCase
) : ViewModel() {

    private val _taskDetails = MutableLiveData<List<TaskFullDetail>>()
    val taskDetails: LiveData<List<TaskFullDetail>> = _taskDetails

    fun loadTaskDetails(dispatchEmployeeId: Int) {
        viewModelScope.launch {
            val result = getTaskWithDetailsUseCase(dispatchEmployeeId)
            _taskDetails.value = result
        }
    }
}
