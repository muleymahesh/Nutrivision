package com.maks.nutrivision.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maks.nutrivision.data.ProductRepository
import com.maks.nutrivision.data.entities.Banner
import com.maks.nutrivision.data.entities.Category
import com.maks.nutrivision.data.entities.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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


    fun getProducts() {
        viewModelScope.launch {
            _products.postValue(productRepository.getProducts())
        }
    }

    fun getBanner() {
        viewModelScope.launch {
            val result = productRepository.getBanners()
            _banners.postValue(result.bannnerList)
            _categories.postValue(result.categoryList)
        }
    }
}
