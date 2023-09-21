package com.example.easy_study.presentation.screen.main

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.easy_study.presentation.navigation.Destination
import com.example.easy_study.presentation.navigation.NavHost
import com.example.easy_study.presentation.navigation.NavigationIntent
import com.example.easy_study.presentation.navigation.composable
import com.example.easy_study.presentation.screen.login.LoginScreen
import com.example.easy_study.presentation.screen.login.LoginViewModel
import com.example.easy_study.presentation.screen.registration.RegistrationScreen
import com.example.easy_study.presentation.screen.registration.RegistrationViewModel
import com.example.easy_study.presentation.ui.theme.EasyStudyTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    NavigationEffects(
        navigationChannel = mainViewModel.navigationChannel,
        navHostController = navController
    )

    EasyStudyTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Destination.LoginScreen
            ) {
                composable(destination = Destination.LoginScreen) {
                    val viewModel = hiltViewModel<LoginViewModel>()
                    val state by viewModel.screenState.collectAsState()
                    LoginScreen(
                        screenState = state,
                        login = viewModel::login,
                        navigateToRegistration = viewModel::navigateToRegistration,
                        validateEmail = viewModel::isEmailValid,
                        validatePassword = viewModel::isPasswordValid
                    )
                }
                composable(destination = Destination.RegistrationScreen) {
                    val viewModel = hiltViewModel<RegistrationViewModel>()
                    val state by viewModel.screenState.collectAsState()
                    RegistrationScreen(
                        screenState = state,
                        register = viewModel::register,
                        navigateBack = viewModel::navigateBack,
                        validateEmail = viewModel::validateEmail,
                        validateUsername = viewModel::validateUsername,
                        validatePassword = viewModel::validatePassword
                    )
                }
                composable(destination = Destination.GroupListScreen) {
                    Text("Group List Screen")
                }
            }
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}