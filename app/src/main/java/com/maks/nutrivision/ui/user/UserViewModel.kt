package com.maks.nutrivision.ui.user

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel@Inject constructor(val authRepository: AuthRepository) :ViewModel() {

    var authState by mutableStateOf(AuthState(false, profile = null, errorMsg = ""))
        private set
    val _profileList = MutableStateFlow<List<Profile>>(emptyList())
    val profileList: StateFlow<List<Profile>> = _profileList


    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var mobile by mutableStateOf("")
    var address by mutableStateOf("")
    var pincode by mutableStateOf("")

    fun updateName(input: String) {
        name = input
    }

    fun updateEmail(input: String) {
        email = input
    }

    fun updateMobile(input: String) {
        mobile = input
    }

    fun updateAddress(input: String) {
        address = input
    }

    fun updatePincode(input: String) {
        pincode = input
    }

    init {
        getAllUsers()
    }

    fun getAllUsers() {
        viewModelScope.launch {
            authRepository.getAllUsers().collectLatest {
                it.firstOrNull().let { profile ->
                    if (profile != null) {
                        authState =
                            authState.copy(isLoading = false, isLogged = true, profile = profile)
                        name = profile.fname
                        email = profile.user_email
                        mobile = profile.mobile
                        address = profile.address
                        pincode = profile.pincode
                    }
                }
            }

        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                authState = authState.copy(isLoading = true, errorMsg = "")
                val response = authRepository.login(LoginRequest("login", email, password))
                if (response.result?.contains("success") == true) {
                    insertUser(response)
                    authState = authState.copy(
                        isLoading = false,
                        errorMsg = "success",
                        isLogged = true,
                        profile = response.toProfile()
                    )
                } else {
                    authState = authState.copy(
                        isLoading = false,
                        errorMsg = "Error: Invalid username or password"
                    )
                }

            } catch (e: Exception) {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error: Invalid username or password"
                )
            }
        }
    }

    private fun insertUser(user: AuthResponse) {
        viewModelScope.launch {
            authRepository.insertUser(user)
        }
    }

    fun validateAddress(
        address: String,
        pincode: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        mobile: String
    ): Boolean {
        return when {

            username.isEmpty() -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"User name cannot be empty."}"
                )
                false
            }

            email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Please enter a valid email address."}"
                )
                false
            }

            mobile.length != 10 || !mobile.matches(Regex("\\d+")) -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Mobile number must be 10 digits."}"
                )
                false
            }

            password.length < 6 -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Password must be at least 6 characters long."}"
                )
                false
            }

            password != confirmPassword -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Passwords do not match."}"
                )
                false
            }

            address.isEmpty() -> {
                authState =
                    authState.copy(isLoading = false, errorMsg = "Error:${"Enter address!"}")
                false
            }

            address.isEmpty() -> {
                authState =
                    authState.copy(isLoading = false, errorMsg = "Error:${"Enter address!"}")
                false
            }

            pincode.length != 6 -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Zip code must be 6 digits."}"
                )

                false
            }

            !pincode.matches(Regex("\\d+")) -> {
                authState = authState.copy(
                    isLoading = false,
                    errorMsg = "Error:${"Zip code must contain only digits."}"
                )

                false
            }

            else -> {
                true
            }
        }
    }

    fun signup(
        action: () -> Unit,
        fname: String,
        lname: String,
        email: String,
        password: String,
        confirmPassword: String,
        mobile: String,
        address: String = "",
        pincode: String,
    ) {
        if (validateAddress(address, pincode, fname, email, password, confirmPassword, mobile)) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    authState = authState.copy(isLoading = true,)
                    val response = authRepository.signup(
                        SignupRequest(
                            "signup",
                            fname = fname,
                            lname = lname,
                            email = email,
                            password = password,
                            mobile = mobile,
                            device_token = "",
                            address = address,
                            pincode = pincode
                        )
                    )
                    authState = authState.copy(false,)
                    if (response.result?.contains("success") == true) {
                        authState = authState.copy(isLoading = false, errorMsg = "success")
                        action
                    } else {
                        authState = authState.copy(
                            isLoading = false,
                            errorMsg = "Error:${response.responseMessage}"
                        )
                    }
                } catch (e: Exception) {
                    authState = authState.copy(
                        isLoading = false,
                        errorMsg = "Error: unable to signup. Please try after sometime"
                    )
                }
            }
        }
    }
}

