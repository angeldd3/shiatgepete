package com.lasec.monitoreoapp.domain.usecase

import com.lasec.monitoreoapp.domain.model.User
import com.lasec.monitoreoapp.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(nombre: String, numero: String): User? {
        return repository.login(nombre, numero)
    }
}
