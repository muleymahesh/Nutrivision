package com.maks.nutrivision.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel@Inject constructor(val cartRepository: CartRepository, val productRepository: ProductRepository) : ViewModel() {
   private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart
    fun getCartProducts() {
        viewModelScope.launch {
            val result = cartRepository.getAllProducts()
            var p_ids =""
            for (product in result) {
               p_ids = p_ids.plus(product.p_id).plus(",") }

            if(p_ids.length<2)
                return@launch
            val products = productRepository.getProductsByIds(p_ids.substring(0,p_ids.length-1))
           val list = mutableListOf<Product>()
            for (p in products) {
                result.find { it.p_id == p.p_id }?.let { list.add( p.copy(count = it.count))}
            }
            _cart.postValue(list)
        }
    }
    fun addToCart(product: Product) {
        viewModelScope.launch {
           insertOrUpdate(product)
        }
    }

    suspend fun insertOrUpdate( item:Product): Boolean {
        val itemsFromDB = cartRepository.getAllProducts().map { it.p_id.equals(item.p_id) }.isEmpty()
        if (itemsFromDB)
            cartRepository.insertProduct(item)
        else {
            item.count+=1
            cartRepository.updateProduct(item)
        }
        return true
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch { cartRepository.deleteProduct(product)
            getCartProducts()
        }
    }
}