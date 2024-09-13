package com.maks.nutrivision.ui.user

import com.maks.nutrivision.data.entities.AuthResponse

data class AuthState(
    val isLoading: Boolean=false,
    val response: AuthResponse?,
)