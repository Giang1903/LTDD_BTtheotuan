package com.example.bttuan04.data

// Đại diện cho một cuốn sách
data class Book(
    val id: Int,
    val title: String
)

// Đại diện cho một sinh viên và các sách sv đã mượn
data class Student(
    val id: Int,
    val name: String,
    val borrowedBookIds: List<Int> = emptyList() // Danh sách ID của sách đã mượn
)