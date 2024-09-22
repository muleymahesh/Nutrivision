package com.maks.nutrivision.ui.order


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.maks.nutrivision.data.entities.Product
import com.maks.nutrivision.data.remote.PlaceOrderParams
import com.maks.nutrivision.data.repositories.AuthRepository
import com.maks.nutrivision.data.repositories.CartRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import com.maks.nutrivision.ui.common.Screen
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
    getCartProducts()
}
    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllUsers().collectLatest {
                it.forEach { it1 ->
                    state = state.copy(
                        isLoading = false,
                        mobile = it1.mobile,
                        name = "${it1.fname} ${it1.lname}",
                        address = it1.address,
                        email = it1.user_email,
                        id = it1.user_id
                    )

                }

            }
        }
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
                    state = state.copy(isLoading = false, productList = list)
                }
                else {
                    state = state.copy(isLoading = false, productList = it)
                }
            }
        }
    }
    fun placeOrder(navHostController: NavHostController) {
        viewModelScope.launch(Dispatchers.IO) {
            var p_ids = ""
            var qty = ""
            var price = ""
            state.productList.forEach {
                p_ids = p_ids.plus(it.p_id).plus(",")
                qty = qty.plus((it.count+1)).plus(",")
                price = price.plus(it.mrp).plus(",")
            }
            val total = state.productList?.sumOf { it.mrp.toInt()*(it.count+1) }

           val result = cartRepository.placeOrder(
                PlaceOrderParams(
                    amount = "$total",
                    city = "${state.address}",
                    email = "${state.email}",
                    first_name = "${state.name}",
                    last_name = "",
                    method = "add_order",
                    p_id = p_ids,
                    phone = "${state.mobile}",
                    price = price,
                    qty = qty,
                    shipping_type = "CASH ON DELIVERY",
                    user_id = "${state.id}"))
            if (result.result.equals("success")) {
                cartRepository.deleteAll()
                viewModelScope.launch(Dispatchers.Main) { navHostController.navigate(Screen.OrderSuccess.route)
                }
            }
        }
    }
}