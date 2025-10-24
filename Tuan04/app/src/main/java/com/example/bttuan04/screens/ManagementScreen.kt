package com.example.bttuan04.screens

import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.bttuan04.data.Book
import com.example.bttuan04.navigation.BottomNavItem
import com.example.bttuan04.viewmodel.LibraryViewModel

@Composable
fun ManagementScreen(navController: NavController, viewModel: LibraryViewModel) {
    val student by viewModel.selectedStudent.collectAsState()
    val borrowedBooks by viewModel.borrowedBooksForSelectedStudent.collectAsState()
    // Cập nhật khi 'student' thay đổi
    var studentNameInput by remember(student.name) { mutableStateOf(student.name) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hệ thống Quản lý Thư viện", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        // --- Khu vực thay đổi Sinh viên ---
        Text("Sinh viên", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = studentNameInput,
                onValueChange = { studentNameInput = it },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { viewModel.updateStudentName(studentNameInput) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Thay đổi")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // --- Khu vực Danh sách sách đã mượn ---
        Text("Danh sách sách (đã mượn)", modifier = Modifier.fillMaxWidth(), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Chiếm hết không gian còn lại
                .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (borrowedBooks.isEmpty()) {
                Text(
                    "Bạn chưa mượn quyển sách nào.\nNhấn 'Thêm' để bắt đầu hành trình đọc sách!",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(borrowedBooks, key = { it.id }) { book ->
                        BookBorrowItem(
                            book = book,
                            onReturnBook = { viewModel.returnBook(book.id) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút "Thêm" sẽ chuyển qua tab DS Sách
        Button(
            onClick = {
                navController.navigate(BottomNavItem.BookList.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm")
        }
    }
}

@Composable
fun BookBorrowItem(book: Book, onReturnBook: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox để trả sách
        Checkbox(
            checked = true, // Luôn checked vì đây là sách đang mượn
            onCheckedChange = { isChecked ->
                if (!isChecked) { // Nếu người dùng bỏ check
                    onReturnBook() // Gọi hàm trả sách
                }
            }
        )
        Text(book.title, modifier = Modifier.padding(start = 8.dp))
    }
}