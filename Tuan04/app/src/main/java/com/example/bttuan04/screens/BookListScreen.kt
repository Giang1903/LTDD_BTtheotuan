package com.example.bttuan04.screens

import androidx.compose.foundation.layout.Arrangement
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
import com.example.bttuan04.data.Book
import com.example.bttuan04.viewmodel.LibraryViewModel

@Composable
fun BookListScreen(viewModel: LibraryViewModel) {
    val allBooks by viewModel.allBooks.collectAsState()
    val student by viewModel.selectedStudent.collectAsState()

    // Lấy ID sách đã mượn để vô hiệu hóa nút "Mượn"
    val borrowedBookIds = student.borrowedBookIds.toSet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Danh sách Sách Thư viện", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Đang mượn sách cho: ${student.name}", fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(allBooks, key = { it.id }) { book ->
                BookAvailableItem(
                    book = book,
                    hasBorrowed = book.id in borrowedBookIds, // Kiểm tra đã mượn chưa
                    onBorrow = { viewModel.borrowBook(book.id) }
                )
            }
        }
    }
}

@Composable
fun BookAvailableItem(
    book: Book,
    hasBorrowed: Boolean,
    onBorrow: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(book.title, modifier = Modifier.weight(1f))
            Button(
                onClick = onBorrow,
                enabled = !hasBorrowed // Vô hiệu hóa nếu đã mượn
            ) {
                Text(if (hasBorrowed) "Đã mượn" else "Mượn")
            }
        }
    }
}