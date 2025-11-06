package com.example.btvntuan06.data

// 1. Lớp "bọc" API chung
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val message: String,
    val data: T
)

// 2. Các lớp con (giống hệt JSON)
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)

data class Reminder(
    val id: Int,
    val time: String,
    val type: String
)

// 3. Model cho Danh sách Task (GET /tasks)
// Đây là đối tượng nằm trong mảng "data" của JSON thứ nhất
data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,

    // **THAY ĐỔI QUAN TRỌNG:** Thêm trường này với giá trị mặc định
    // để khớp với thiết kế UI (checkbox) mà không làm crash app.
    val isCompleted: Boolean = false,

    val priority: String,
    val category: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val subtasks: List<Subtask>,
    val attachments: List<Attachment>,
    val reminders: List<Reminder>
)

// 4. Model cho Chi tiết Task (GET /task/{id})
// Đây là đối tượng "data" của JSON thứ hai
data class TaskDetail(
    val id: Int,
    val title: String,
    val desImageURL: String, // URL của ảnh (bị bỏ qua trong UI theo yêu cầu)
    val description: String,
    val status: String,
    val priority: String,
    val category: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val subtasks: List<Subtask>,
    val attachments: List<Attachment>,
    val reminders: List<Reminder>
)

// 5. **MODEL MỚI**
// Model cho Request Body khi tạo Task (POST /tasks)
// Chỉ chứa những trường cần thiết khi tạo mới
data class CreateTaskRequest(
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val dueDate: String
)