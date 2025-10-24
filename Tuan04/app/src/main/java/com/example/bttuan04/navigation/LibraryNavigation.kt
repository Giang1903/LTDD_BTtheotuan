package com.example.bttuan04.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bttuan04.screens.BookListScreen
import com.example.bttuan04.screens.ManagementScreen
import com.example.bttuan04.screens.StudentListScreen
import com.example.bttuan04.viewmodel.LibraryViewModel

// --- Định nghĩa các tab cho Bottom Nav ---
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Management : BottomNavItem("management", "Quản lý", Icons.Default.Home)
    object BookList : BottomNavItem("books", "DS Sách", Icons.Default.List)
    object StudentList : BottomNavItem("students", "Sinh viên", Icons.Default.Person)
}

val bottomNavItems = listOf(
    BottomNavItem.Management,
    BottomNavItem.BookList,
    BottomNavItem.StudentList,
)

// --- Màn hình chính chứa Scaffold và Bottom Nav ---
@Composable
fun LibraryScreen() {
    val viewModel: LibraryViewModel = viewModel()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        LibraryNavHost(
            navController = navController,
            viewModel = viewModel,
            padding = innerPadding
        )
    }
}

// --- Thanh Bottom Nav ---
@Composable
fun BottomNavBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
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

// --- NavHost (Bộ điều hướng cho 3 tab) ---
@Composable
fun LibraryNavHost(
    navController: NavHostController,
    viewModel: LibraryViewModel,
    padding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Management.route, // Tab bắt đầu
        modifier = Modifier.padding(padding)
    ) {
        composable(BottomNavItem.Management.route) {
            ManagementScreen(navController = navController, viewModel = viewModel)
        }
        composable(BottomNavItem.BookList.route) {
            BookListScreen(viewModel = viewModel)
        }
        composable(BottomNavItem.StudentList.route) {
            StudentListScreen(navController = navController, viewModel = viewModel)
        }
    }
}