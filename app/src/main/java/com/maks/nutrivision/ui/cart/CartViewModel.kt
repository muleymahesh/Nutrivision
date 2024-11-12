package com.maks.nutrivision.ui.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.entities.CartItem
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
            try {
                cartRepository.getAllCartItems().collectLatest {
                    if (it.isNotEmpty()) {
                        val list = mutableListOf<Product>()

                        it.forEach {
                            Log.e("@@@@@",it.selectedIndex.toString())
                            p_ids = p_ids.plus(it.p_id).plus(",")
                        }
                        val productResponse =
                            productRepository.getProductsByIds(p_ids.substring(0, p_ids.length - 1))
                        for (p in it) {
                            productResponse.data.find { product -> p.p_id == product.p_id }
                                ?.let { pro->
                                    list.add(pro.copy(opt1_count = p.opt1_count,opt2_count = p.opt2_count,opt3_count = p.opt3_count, selectedIndex = p.selectedIndex))
                                }
                        }
                        state = state.copy(isLoading = false, products = list, deliveryCharges = productResponse.delivery_charges, handlingCharges = productResponse.handling_charges)
                    } else {
                        state = state.copy(isLoading = false, products = listOf())
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

//    fun deleteProduct(product: Product) {
//        viewModelScope.launch { cartRepository.deleteCartItem(CartItem(p_id = product.p_id, count = 1, selectedIndex = 0)) }
//            getCartProducts()
//        }
//    }
}
