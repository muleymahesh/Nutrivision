package com.maks.nutrivision.ui.order


import android.util.Log
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
import com.maks.nutrivision.data.repositories.OrderRepository
import com.maks.nutrivision.data.repositories.ProductRepository
import com.maks.nutrivision.ui.common.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceOrderViewModel@Inject constructor(val cartRepository: CartRepository, val productRepository: ProductRepository,
    val authRepository: AuthRepository, val orderRepository: OrderRepository
) : ViewModel() {

    var state by mutableStateOf(PlaceOrderState(isLoading = true))
        private set

    var orderState by mutableStateOf(MyOrderState(isLoading = true))
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
                        id = it1.user_id,
                        pincode = it1.pincode
                    )

                }

            }
        }
    }

    fun getCartProducts() {
        viewModelScope.launch {
            var p_ids =""
            cartRepository.getAllCartItems().collectLatest {
                if (it.isNotEmpty()) {
                    val list = mutableListOf<Product>()

                    it.forEach {
                        Log.e("@@@@@",it.selectedIndex.toString())
                        p_ids = p_ids.plus(it.p_id).plus(",")
                    }
                    val productsResponse =
                        productRepository.getProductsByIds(p_ids.substring(0, p_ids.length - 1))
                    for (p in it) {
                        productsResponse.data.find { product -> p.p_id == product.p_id }
                            ?.let { pro->
                                list.add(pro.copy(opt1_count = p.opt1_count,opt2_count = p.opt2_count,opt3_count = p.opt3_count, selectedIndex = p.selectedIndex))
                            }
                    }
                    state = state.copy(isLoading = false, productList = list)
                } else {
                    state = state.copy(isLoading = false, productList = listOf())
                }

            }
        }
    }
    fun placeOrder(navHostController: NavHostController,total: Double,shippingType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var p_ids = ""
            var qty = ""
            var price = ""
            var opt_name = ""
            state.productList.forEach {
                p_ids = p_ids.plus(it.p_id).plus(",")
                qty = qty.plus((it.getSelectedCount())).plus(",")
                price = price.plus(it.getPrice()).plus(",")
                opt_name = opt_name.plus(it.getNamedWeight()).plus(",")
            }

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
                    opt_name = opt_name,
                    shipping_type = shippingType,
                    user_id = "${state.id}"))
            if (result.result.equals("success")) {
                cartRepository.deleteAll()
                viewModelScope.launch(Dispatchers.Main) {
                    navHostController.navigate(Screen.OrderSuccess.route)
                }
            }
        }
    }

    fun getMyOrders(user_email:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val list = orderRepository.getMyOrders(user_email)
                orderState = orderState.copy(isLoading = false, orderList = list)
            }catch (e:Exception){
                orderState = orderState.copy(isLoading = false)
                e.printStackTrace()
            }

        }
    }
}