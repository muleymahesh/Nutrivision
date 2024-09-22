package com.maks.nutrivision.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.repositories.ProductRepository
import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductViewModel@Inject constructor(val productRepository: ProductRepository, val cartRepository: CartRepository) : ViewModel() {

 var state by mutableStateOf(HomeState())
    private set

 private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart

    fun getProducts(cat_id: String?) {
        viewModelScope.launch {
            try {
                state = state.copy(isLoading = true)

                val result = productRepository.getProducts(cat_id)
                state = state.copy(isLoading = false,products = result)

            }catch (e:Exception){
                state = state.copy(isLoading = false,error = e.message.toString())
            }

        }
    }

    fun getBanner() {
        viewModelScope.launch {
            val result = productRepository.getBanners()
            state = state.copy(banners = result.bannnerList,categories = result.categoryList,isLoading = false)
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            insertOrUpdate(product)
        }
    }

    private fun insertOrUpdate(item:Product) {
        viewModelScope.launch {
            val result = cartRepository.getAllProducts().first()

                val item1 = result.firstOrNull { it.p_id == item.p_id }
                if (item1 == null) {
                    cartRepository.insertProduct(item)
                    Log.d("TAG", "inserted")
                } else {
                    item1.count += 1
                    cartRepository.updateProduct(item1)
                    Log.d("TAG", "updated")

                }

            }
        }
}
