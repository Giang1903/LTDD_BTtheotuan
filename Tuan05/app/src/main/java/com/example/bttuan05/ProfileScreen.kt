package com.example.bttuan05 // ⚠️ THAY BẰNG TÊN PACKAGE CỦA BẠN

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.bttuan05.Navigation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val user = Firebase.auth.currentUser
    val targetBlue = Color(0xFF81B3E8)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        Firebase.auth.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Profile.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // Ảnh đại diện với icon Camera
            Box(modifier = Modifier.size(120.dp)) {
                AsyncImage(
                    model = user?.photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )

                // Icon Camera
                IconButton(
                    onClick = { /* TODO: Xử lý chọn ảnh */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .background(targetBlue, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera_alt),
                        contentDescription = "Change Picture",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Thông tin người dùng (ĐÃ SỬA MÀU CHỮ)
            ProfileTextField(label = "Name", value = user?.displayName ?: "N/A")
            Spacer(modifier = Modifier.height(16.dp))
            ProfileTextField(label = "Email", value = user?.email ?: "N/A")
            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth (ĐÃ SỬA MÀU CHỮ)
            OutlinedTextField(
                value = "23/05/1995",
                onValueChange = {},
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Black,
                    disabledBorderColor = Color.Gray,
                    disabledLabelColor = Color.Gray,
                    disabledTrailingIconColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Nút Back
            Button(
                onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = targetBlue
                )
            ) {
                Text("Back", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Hàm Composable tùy chỉnh cho TextField (ĐÃ SỬA MÀU CHỮ)
@Composable
private fun ProfileTextField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = false, // <-- THÊM DÒNG NÀY
        colors = OutlinedTextFieldDefaults.colors( // <-- THÊM KHỐI NÀY
            disabledTextColor = Color.Black,
            disabledBorderColor = Color.Gray,
            disabledLabelColor = Color.Gray
        )
    )
}