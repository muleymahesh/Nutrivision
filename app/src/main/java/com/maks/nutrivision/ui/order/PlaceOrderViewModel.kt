package com.maks.nutrivision.ui.order


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.repositories.AuthRepository
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceOrderViewModel@Inject constructor(val cartRepository: CartRepository, val productRepository: ProductRepository,
    val authRepository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf(PlaceOrderState(isLoading = true))
        private set
init {
    getAllUsers()
}
    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllUsers().collectLatest {
                it.forEach { it1 ->
                    state = state.copy(
                        isLoading = false,
                        mobile = it1.mobile,
                        name = "${it1.fname} ${it1.lname}",
                        address = it1.address
                    )

                }
               cartRepository.getAllProducts().collectLatest {
                    state = state.copy(isLoading = false, productList = it)
                }

            }
        }
    }
}