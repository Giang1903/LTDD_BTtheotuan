package com.example.btvntuan06.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.btvntuan06.ui.screens.AddTaskScreen // <-- THÊM IMPORT
import com.example.btvntuan06.ui.screens.TaskDetailScreen
import com.example.btvntuan06.ui.screens.TaskListScreen

// Định nghĩa các đường dẫn (routes)
object Routes {
    const val TASK_LIST = "taskList"
    const val TASK_DETAIL = "taskDetail" // Đường dẫn cơ sở
    const val ARG_TASK_ID = "taskId" // Tên của tham số

    // Đường dẫn đầy đủ cho màn hình chi tiết, ví dụ: "taskDetail/123"
    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL/{$ARG_TASK_ID}"

    const val ADD_TASK = "addTask" // <-- THÊM ĐƯỜNG DẪN MỚI
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.TASK_LIST) {

        // 1. Màn hình Danh sách (List)
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                onTaskClick = { taskId ->
                    // Điều hướng tới màn hình chi tiết, truyền ID
                    navController.navigate("${Routes.TASK_DETAIL}/$taskId")
                },
                onAddTaskClick = { // <-- THÊM HÀNH ĐỘNG NÀY
                    navController.navigate(Routes.ADD_TASK)
                }
            )
        }

        // 2. Màn hình Chi tiết (Detail)
        composable(
            route = Routes.TASK_DETAIL_ROUTE,
            arguments = listOf(navArgument(Routes.ARG_TASK_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            // Lấy taskId từ argument
            val taskId = backStackEntry.arguments?.getString(Routes.ARG_TASK_ID)

            requireNotNull(taskId) { "Task ID không được null" }

            TaskDetailScreen(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() },
                onTaskDeleted = {
                    // Sau khi xóa thành công, quay về màn hình trước (List)
                    navController.popBackStack()
                }
            )
        }

        // 3. MÀN HÌNH MỚI: Thêm Task
        composable(Routes.ADD_TASK) {
            AddTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}