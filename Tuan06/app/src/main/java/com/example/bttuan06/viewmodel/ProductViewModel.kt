package com.example.bttuan06.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bttuan06.data.ApiClient
import com.example.bttuan06.data.Product
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log

class ProductViewModel : ViewModel() {
    var product by mutableStateOf<Product?>(null)
        private set

    init {
        loadNewProduct()
    }

    fun loadNewProduct() {
        product = null

        viewModelScope.launch {
            try {
                val result = ApiClient.productApi.getProduct()
                Log.d("API_RESULT", result.toString())
                product = result
            } catch (e: Exception) {
                Log.e("API_ERROR", "Lỗi khi gọi API", e)
            }
        }
    }
}