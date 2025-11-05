// MainActivity.kt
package com.example.bttuan06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bttuan06.ui.ProductDetailScreen
import com.example.bttuan06.ui.ProductListScreen
import com.example.bttuan06.ui.theme.BTtuan06Theme
import com.example.bttuan06.viewmodel.ProductViewModel

// Định nghĩa các Route (đường dẫn)
object Destinations {
    const val LIST = "product_list"
    const val DETAIL = "product_detail"
}

class MainActivity : ComponentActivity() {
    // Chỉ tạo ViewModel một lần
    private val viewModel = ProductViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BTtuan06Theme {
                // Tạo NavController để quản lý trạng thái điều hướng
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Destinations.LIST
                ) {
                    // Màn hình 1: Danh sách Sản phẩm
                    composable(Destinations.LIST) {
                        ProductListScreen(
                            onNavigateToDetail = {
                                // Gọi hàm để tải sản phẩm mới trước khi điều hướng
                                viewModel.loadNewProduct()
                                // Điều hướng sang màn hình chi tiết khi nhấn nút
                                navController.navigate(Destinations.DETAIL)
                            }
                        )
                    }

                    // Màn hình 2: Chi tiết Sản phẩm
                    composable(Destinations.DETAIL) {
                        ProductDetailScreen(
                            viewModel = viewModel,
                            onBackClicked = {
                                // Quay lại màn hình trước đó
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}