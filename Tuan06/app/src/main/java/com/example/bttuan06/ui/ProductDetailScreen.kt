// ProductDetailScreen.kt (Giữ nguyên phiên bản đã sửa trước đó)
package com.example.bttuan06.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bttuan06.viewmodel.ProductViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme.colorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel,
    onBackClicked: () -> Unit
) {
    val product = viewModel.product

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Product detail", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorScheme.surface
                )
            )
        },
        content = { paddingValues ->
            Surface(modifier = Modifier.fillMaxSize()) {
                if (product != null) {
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 1. Ảnh sản phẩm
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.8f))
                        ) {
                            AsyncImage(
                                model = product.imgURL, // Đảm bảo imgURL hợp lệ
                                contentDescription = "Product Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // 2. Tên và Giá
                        Column(modifier = Modifier.padding(24.dp, 16.dp)) {
                            Text(
                                text = product.name,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Giá: ${product.price}đ",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                        }

                        // 3. Mô tả
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = product.des,
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                modifier = Modifier.padding(16.dp),
                                lineHeight = 20.sp
                            )
                        }
                    }
                } else {
                    // Hiển thị vòng tròn loading khi product là null (trong lúc đang gọi API mới)
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}