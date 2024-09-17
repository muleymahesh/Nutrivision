package com.maks.nutrivision.ui.order


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.entities.Profile
import com.maks.nutrivision.data.repositories.AuthRepository
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PlaceOrderViewModel@Inject constructor(val cartRepository: CartRepository, val productRepository: ProductRepository,
    val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PlaceOrderState())
    val state: StateFlow<PlaceOrderState> = _state

    val _profileList = MutableStateFlow<List<Profile>>(emptyList())
    val profileList: StateFlow<List<Profile>> = _profileList

    private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart
    fun getCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = cartRepository.getAllProducts()
              _cart.postValue(result)
            _state.getAndUpdate { PlaceOrderState(isLoading = false, productList = _cart.value ?: emptyList(), address = profileList.value.firstOrNull()?.address ?: "") }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = authRepository.getAllUsers()
            _profileList.getAndUpdate { list }
            _state.getAndUpdate { PlaceOrderState(isLoading = false, productList = _cart.value ?: emptyList(), address = profileList.value.firstOrNull()?.address ?: "") }

            Log.e(",@@@@@,",_profileList.value.toString())
        }
    }
}