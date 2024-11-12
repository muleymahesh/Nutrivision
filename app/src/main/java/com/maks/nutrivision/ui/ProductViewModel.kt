package com.maks.nutrivision.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.repositories.ProductRepository
import com.maks.nutrivision.data.entities.CartItem
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductViewModel@Inject constructor(val productRepository: ProductRepository, val cartRepository: CartRepository) : ViewModel() {

 var state by mutableStateOf(HomeState())
    private set

 private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart

    fun getProductsByCategory(cat_id: String) {
        try{
            state = state.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProductsByCategory(cat_id).collectLatest {
                state = state.copy(products = it, isLoading = false)
            }

        }
        }catch (e:Exception){
            state = state.copy(isLoading = false,error = e.message.toString())
        }
    }

    fun getBanner() {
        state = state.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val result = productRepository.getBanners()
                val products = productRepository.getProducts(null)
                products.forEach {
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            productRepository.insertProducts(it)

                        } catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                }
                state = state.copy(banners = result.bannnerList,categories = result.categoryList,isLoading = false)

            } catch (e:Exception){
                state = state.copy(isLoading = false,error = e.message.toString())
            }
         }
    }

    fun addToCart(product: Product, selectedIndex: Int) {
        viewModelScope.launch {
            cartRepository.insertCartItem(product,selectedIndex)
            product.cat_id?.let {
                getProductsByCategory(it) };
        }
    }

    fun removeFromCart(product: Product) {
        viewModelScope.launch {
            if (product.getSelectedCount()==1){
                cartRepository.deleteCartItem(CartItem(p_id = product.p_id, opt1_count = 1, opt2_count = 0, opt3_count = 0, selectedIndex = product.selectedIndex))
            }else if (product.getSelectedCount()>1){
                cartRepository.decreaseQty(product.selectedIndex,product.p_id)
            }
        }
    }
}
