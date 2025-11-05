package com.example.bttuan05

class Navigation {
    sealed class Screen(val route: String) {
        data object Splash : Screen("splash")
        data object Login : Screen("login")
        data object Profile : Screen("profile")
    }
}