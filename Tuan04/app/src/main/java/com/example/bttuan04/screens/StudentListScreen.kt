package com.example.bttuan04.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bttuan04.data.Student
import com.example.bttuan04.navigation.BottomNavItem
import com.example.bttuan04.viewmodel.LibraryViewModel

@Composable
fun StudentListScreen(navController: NavController, viewModel: LibraryViewModel) {
    val allStudents by viewModel.allStudents.collectAsState()
    val selectedStudent by viewModel.selectedStudent.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Danh sách Sinh viên", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allStudents, key = { it.id }) { student ->
                StudentItem(
                    student = student,
                    isSelected = student.id == selectedStudent.id,
                    onClick = {
                        // 1. Cập nhật ViewModel
                        viewModel.selectStudent(student.id)

                        // 2. Chuyển về tab Quản lý
                        navController.navigate(BottomNavItem.Management.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StudentItem(
    student: Student,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick), // Dùng onClick từ tham số
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp,
        // Đánh dấu sinh viên đang được chọn
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Text(
            text = student.name,
            modifier = Modifier.padding(16.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}