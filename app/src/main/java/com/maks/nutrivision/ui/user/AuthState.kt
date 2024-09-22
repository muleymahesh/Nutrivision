package com.maks.nutrivision.ui.user

import com.maks.nutrivision.data.entities.AuthResponse
import com.maks.nutrivision.data.entities.Profile

data class AuthState(
    val isLoading: Boolean=false,
    val response: AuthResponse?,
    val isLogged: Boolean=false,
    val profile: Profile?=null
)