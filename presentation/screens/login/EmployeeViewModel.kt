package com.lasec.monitoreoapp.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.domain.usecase.GetEmployeesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EmployeeViewModel @Inject constructor(
    private val getEmployeesUseCase: GetEmployeesUseCase
) : ViewModel() {

    private val _employeeList = MutableLiveData<List<Employee>>()
    val employeeList: LiveData<List<Employee>> = _employeeList

    fun onCreate() {
        viewModelScope.launch {
            val result = getEmployeesUseCase()
            _employeeList.postValue(result ?: emptyList())
        }
    }
}

