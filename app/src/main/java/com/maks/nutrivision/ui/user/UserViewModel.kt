package com.maks.nutrivision.ui.user

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.LoginRequest
import com.maks.nutrivision.data.entities.Profile
import com.maks.nutrivision.data.entities.SignupRequest
import com.maks.nutrivision.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel@Inject constructor(val authRepository: AuthRepository) :ViewModel() {

    var authState by mutableStateOf(AuthState(false,null))
        private set
    val _profileList = MutableStateFlow<List<Profile>>(emptyList())
    val profileList: StateFlow<List<Profile>> = _profileList

    init {
        getAllUsers()
    }
    fun getAllUsers() {
        viewModelScope.launch {
            val list = authRepository.getAllUsers().collectLatest {
                _profileList.getAndUpdate { it }
                it.firstOrNull().let { profile ->
                    authState = authState.copy(isLoading = false, isLogged = true, profile = profile)
                }
            }

        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                authState = authState.copy(isLoading = true ,null)
                val response = authRepository.login(LoginRequest("login",email, password))
                if (response.result?.contains("success") == true){
                    insertUser(response)
                    authState = authState.copy(isLoading = false, response = response , isLogged = true, profile = response.toProfile())
                }

            } catch (e: Exception) {
                authState = authState.copy(isLoading = false, response = AuthResponse("error", e.message ?: "Unknown error","","","",0,"","",""))
            }
        }
    }
    private fun insertUser(user: AuthResponse) {
        viewModelScope.launch {
            authRepository.insertUser(user)
        }
    }
    fun signup(
        action: () -> Unit,
        fname: String,
        lname: String,
        email: String,
        password: String,
        mobile: String,
        address: String=""
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authState = authState.copy(isLoading = true,null)
                val response = authRepository.signup(SignupRequest("signup", fname = fname, lname = lname, email =  email, password = password, mobile = mobile, device_token="",address = address))
                authState = authState.copy(false, response)
                if (response.result?.contains("success") == true){
                    action
                }
            } catch (e: Exception) {
                authState = authState.copy(false,AuthResponse("error", e.message ?: "Unknown error","","","",0,"","",""))
            }
        }
    }
}

