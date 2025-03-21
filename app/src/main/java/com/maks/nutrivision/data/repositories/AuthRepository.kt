package com.maks.nutrivision.data.repositories

import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.ForgotPasswordRequest
import com.maks.nutrivision.data.entities.HomeScreenData
import com.maks.nutrivision.data.entities.LoginRequest
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.Profile
import com.maks.nutrivision.data.entities.SignupRequest
import com.maks.nutrivision.data.local.UserDao
import com.maks.nutrivision.data.remote.ApiService
import com.maks.nutrivision.data.remote.RequestParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthRepository{
    suspend fun login(loginRequest: LoginRequest):AuthResponse
    suspend fun forgotPassword(loginRequest: ForgotPasswordRequest):AuthResponse
    suspend fun signup(signupRequest: SignupRequest): AuthResponse
    suspend fun insertUser(user: AuthResponse)
    fun getAllUsers(): Flow<List<Profile>>
}
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao): AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): AuthResponse {
    return apiService.login(loginRequest)
    }

    override suspend fun forgotPassword(loginRequest: ForgotPasswordRequest): AuthResponse {
        return apiService.forgotPassword(loginRequest)
    }

    override suspend fun signup(signupRequest: SignupRequest): AuthResponse {
       return apiService.signup(signupRequest)
    }

    override suspend fun insertUser(user: AuthResponse) {
        userDao.insertUser(user.toProfile())
    }
    override fun getAllUsers(): Flow<List<Profile>> = userDao.getAllUsers()
}