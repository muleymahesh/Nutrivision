package com.maks.nutrivision.ui.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.LoginRequest
import com.maks.nutrivision.data.entities.Profile
import com.maks.nutrivision.data.entities.SignupRequest
import com.maks.nutrivision.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel@Inject constructor(val authRepository: AuthRepository) :ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState(false,null))
    val authState: StateFlow<AuthState> = _authState
    val _profileList = MutableStateFlow<List<Profile>>(emptyList())
    val profileList: StateFlow<List<Profile>> = _profileList

    fun getAllUsers() {
        viewModelScope.launch {
            val list = authRepository.getAllUsers()
            _profileList.getAndUpdate { list }
            Log.e(",@@@@@,",_profileList.value.toString())
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState(isLoading = true ,null)
                val response = authRepository.login(LoginRequest("login",email, password))
                if (response.result?.contains("success") == true){
                    insertUser(response)
                }
                _authState.value =AuthState(response = response)

            } catch (e: Exception) {
                _authState.value = AuthState(isLoading = false, response = AuthResponse("error", e.message ?: "Unknown error","","","",0,"",""))
            }
        }
    }
    fun insertUser(user: AuthResponse) {
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
        viewModelScope.launch {
            try {
                _authState.value = AuthState(isLoading = true,null)
                val response = authRepository.signup(SignupRequest("signup", fname = fname, lname = lname, email =  email, password = password, mobile = mobile, device_token="",address = address))
                _authState.value = AuthState(false, response)
                if (response.result?.contains("success") == true){
                    action
                }
            } catch (e: Exception) {
                _authState.value = AuthState(false,AuthResponse("error", e.message ?: "Unknown error","","","",0,"",""))
            }
        }
    }
}

