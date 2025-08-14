package com.lasec.monitoreoapp.domain.model.manual_workorders

data class AuthorizedEmployeesDto (

    val indexEmployeeId: Int,
    private val stepWizard:Int = 3,
    val workOrderId: String

)