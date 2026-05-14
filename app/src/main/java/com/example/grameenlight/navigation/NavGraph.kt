package com.example.grameenlight.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.ui.admin.AdminDashboardScreen
import com.example.grameenlight.ui.admin.AdminReportDetailsScreen
import com.example.grameenlight.ui.admin.AdminViewModel
import com.example.grameenlight.ui.auth.LoginScreen
import com.example.grameenlight.ui.auth.RegisterScreen
import com.example.grameenlight.ui.dashboard.DashboardScreen
import com.example.grameenlight.ui.feedback.FeedbackScreen
import com.example.grameenlight.ui.map.MapScreen
import com.example.grameenlight.ui.profile.ProfileScreen
import com.example.grameenlight.ui.report.ReportScreen
import com.example.grameenlight.ui.splash.SplashScreen
import com.example.grameenlight.ui.tracker.RepairTrackerScreen

sealed class Screen(val route: String) {
    object Splash   : Screen("splash")
    object Login    : Screen("login")
    object Register : Screen("register")
    object Dashboard: Screen("dashboard")
    object Report   : Screen("report")
    object Feedback : Screen("feedback")
    object Map      : Screen("map")
    object Tracker  : Screen("tracker")
    object Profile  : Screen("profile")
    object Admin    : Screen("admin")
}

@Composable
fun GrameenNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {

            SplashScreen(

                onReady = { isLoggedIn, role ->

                    val destination = if (isLoggedIn) {

                        if (role == "admin") {

                            Screen.Admin.route

                        } else {

                            Screen.Dashboard.route
                        }

                    } else {

                        Screen.Login.route
                    }

                    navController.navigate(destination) {

                        popUpTo(Screen.Splash.route) {

                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { role ->
                    val dest = if (role == "admin")
                        Screen.Admin.route
                    else
                        Screen.Dashboard.route
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                onSuccess     = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }
        composable(Screen.Report.route) {
            ReportScreen(navController)
        }
        composable(Screen.Feedback.route) {
            FeedbackScreen(navController)
        }
        composable(Screen.Map.route) {
            MapScreen(navController)
        }
        composable(Screen.Tracker.route) {
            RepairTrackerScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Admin.route) {
            AdminDashboardScreen(navController)
        }
        
        composable(
            route = "admin_report_details/{reportId}",
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId")
            val adminViewModel: AdminViewModel = viewModel()
            val uiState by adminViewModel.uiState.collectAsState()
            val report = uiState.reports.find { it.reportId == reportId }
            
            if (report != null) {
                AdminReportDetailsScreen(
                    report = report,
                    navController = navController,
                    viewModel = adminViewModel
                )
            }
        }
    }
}
