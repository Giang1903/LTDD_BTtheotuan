package com.example.bttuan04.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bttuan04.data.Book
import com.example.bttuan04.data.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LibraryViewModel : ViewModel() {

    // --- NGUỒN DỮ LIỆU GỐC (MASTER LISTS) ---

    private val _allBooks = MutableStateFlow(
        listOf(
            Book(1, "Sách 01"),
            Book(2, "Sách 02"),
            Book(3, "Sách 03 - Lập trình Android"),
            Book(4, "Sách 04 - Cấu trúc dữ liệu")
        )
    )
    val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()

    private val _allStudents = MutableStateFlow(
        listOf(
            Student(1, "Nguyen Van A", borrowedBookIds = listOf(1, 2)),
            Student(2, "Nguyen Thi B", borrowedBookIds = listOf(1)),
            Student(3, "Nguyen Van C", borrowedBookIds = emptyList())
        )
    )
    val allStudents: StateFlow<List<Student>> = _allStudents.asStateFlow()


    // --- TRẠNG THÁI (STATE) HIỆN TẠI ---

    private val _selectedStudentId = MutableStateFlow(_allStudents.value.first().id)

    val selectedStudent: StateFlow<Student> = _selectedStudentId.combine(_allStudents) { id, students ->
        students.find { it.id == id }!!
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _allStudents.value.first())

    val borrowedBooksForSelectedStudent: StateFlow<List<Book>> = selectedStudent.combine(allBooks) { student, books ->
        books.filter { it.id in student.borrowedBookIds }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    // --- SỰ KIỆN (EVENTS) / HÀNH ĐỘNG ---

    fun selectStudent(studentId: Int) {
        _selectedStudentId.value = studentId
    }

    fun updateStudentName(newName: String) {
        val studentId = _selectedStudentId.value
        _allStudents.value = _allStudents.value.map { student ->
            if (student.id == studentId) {
                student.copy(name = newName)
            } else {
                student
            }
        }
    }

    fun borrowBook(bookId: Int) {
        val studentId = _selectedStudentId.value
        _allStudents.value = _allStudents.value.map { student ->
            if (student.id == studentId && bookId !in student.borrowedBookIds) {
                student.copy(borrowedBookIds = student.borrowedBookIds + bookId)
            } else {
                student
            }
        }
    }

    fun returnBook(bookId: Int) {
        val studentId = _selectedStudentId.value
        _allStudents.value = _allStudents.value.map { student ->
            if (student.id == studentId) {
                student.copy(borrowedBookIds = student.borrowedBookIds - bookId)
            } else {
                student
            }
        }
    }
}