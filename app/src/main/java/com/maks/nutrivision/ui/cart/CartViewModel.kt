package com.maks.nutrivision.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel@Inject constructor(val cartRepository: CartRepository, val productRepository: ProductRepository) : ViewModel() {
    var state by mutableStateOf(CartState(isLoading = true))
        private set

    init {
        getCartProducts()
    }
    fun getCartProducts() {
        viewModelScope.launch {
            var p_ids =""
            cartRepository.getAllProducts().collectLatest {
                if (it.isNotEmpty()) {
                it.forEach {
                    p_ids = p_ids.plus(it.p_id).plus(",")
                }
                val products = productRepository.getProductsByIds(p_ids.substring(0,p_ids.length-1))
                val list = mutableListOf<Product>()
                for (p in products) {
                    it.find { it.p_id == p.p_id }?.let { list.add( p.copy(count = it.count))}
                }
                state = state.copy(isLoading = false,products = list)
                }
                else {
                    state = state.copy(isLoading = false,products = it)
                }
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            insertOrUpdate(product)
        }
    }

    suspend fun insertOrUpdate( item:Product): Boolean {
        cartRepository.getAllProducts().collectLatest {
            val isinCart = it.find { it.p_id == item.p_id } != null
            if (!isinCart)
                cartRepository.insertProduct(item)
            else {
                item.count+=1
                cartRepository.updateProduct(item)
            }
        }

        return true
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch { cartRepository.deleteProduct(product)
            getCartProducts()
        }
    }
}
