package com.maks.nutrivision.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.ProductRepository
import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.ui.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProductViewModel@Inject constructor(val productRepository: ProductRepository) : ViewModel() {

    private val _banners = MutableLiveData<List<Banner>>()
    val banner: LiveData<List<Banner>>
        get() = _banners

    private val _categories = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>>
        get() = _categories
    private val _products = MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>>
        get() = _products

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun getProducts(cat_id: String?) {
        viewModelScope.launch {
            try {
                _state.value = HomeState(isLoading = true)

                val result = productRepository.getProducts(cat_id)
                _state.value = HomeState(products = result)

            }catch (e:Exception){
                _state.value = HomeState(error = e.message.toString())
            }

        }
    }

    fun getBanner() {
        viewModelScope.launch {
            val result = productRepository.getBanners()
            _banners.postValue(result.bannnerList)
            _categories.postValue(result.categoryList)
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            productRepository.addToCart(product)
        }
    }
    fun getCartProducts() {
        viewModelScope.launch {
            val result = productRepository.getCartItems()
            _state.value = HomeState(products = result ?: emptyList())
        }
    }
}
