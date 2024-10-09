package com.example.freevote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freevote.ui.screens.CreatePinScreen
import com.example.freevote.ui.screens.HomeScreen
import com.example.freevote.ui.screens.PinScreen
import com.example.freevote.ui.screens.RegistrationScreen
import com.example.myapplication.IdNumberScreen
import com.example.myproject1.ui.theme.FreeVoteTheme
import androidx.activity.viewModels
import com.example.freevote.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // Use activity-scoped ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreeVoteTheme {
                Navigation(viewModel) // Pass the ViewModel to the Navigation
            }
        }
    }
}

@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "idNumberScreen") {
        composable("idNumberScreen") { IdNumberScreen(navController, viewModel) }
        composable("homeScreen") { HomeScreen(Modifier, navController, viewModel) }
        composable(
            route = "registrationScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            // Use the same instance of ViewModel passed from MainActivity
            RegistrationScreen(
                navController = navController,
                idNumber = idNumber,
                viewModel = viewModel // Use the activity-scoped ViewModel
            )
        }
        composable(
            route = "pinScreen/{ID_NUMBER}",
            arguments = listOf(navArgument("ID_NUMBER") { type = NavType.StringType })
        ) { backStackEntry ->
            val idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: ""
            PinScreen(
                navController = navController,
                idNumber = idNumber,
                viewModel = viewModel,
                modifier = Modifier
            )
        }

        composable(
            route = "CreatePinScreen/{ID_NUMBER}/{LAST_NAME}/{NAMES}/{PHONE_NUMBER}/{EMAIL}/{GENDER}/{ADDRESS}",
            arguments = listOf(
                navArgument("ID_NUMBER") { type = NavType.StringType },
                navArgument("LAST_NAME") { type = NavType.StringType },
                navArgument("NAMES") { type = NavType.StringType },
                navArgument("PHONE_NUMBER") { type = NavType.StringType },
                navArgument("EMAIL") { type = NavType.StringType },
                navArgument("GENDER") { type = NavType.StringType },
                navArgument("ADDRESS") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            CreatePinScreen(
                navController = navController,
                idNumber = backStackEntry.arguments?.getString("ID_NUMBER") ?: "",
                lastName = backStackEntry.arguments?.getString("LAST_NAME") ?: "",
                names = backStackEntry.arguments?.getString("NAMES") ?: "",
                phoneNumber = backStackEntry.arguments?.getString("PHONE_NUMBER") ?: "",
                email = backStackEntry.arguments?.getString("EMAIL") ?: "",
                gender = backStackEntry.arguments?.getString("GENDER") ?: "",
                address = backStackEntry.arguments?.getString("ADDRESS") ?: "",
                viewModel = viewModel
            )
        }
    }
}
