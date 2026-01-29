package com.example.otpauth.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.otpauth.ui.theme.OtpAuthTheme
import com.example.otpauth.viewmodel.AuthUiState
import com.example.otpauth.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    
    // In a larger app, we would use Hilt: @AndroidEntryPoint and viewModels()
    // For this assignment, we use the default factory which is sufficient.
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OtpAuthTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState by viewModel.uiState.collectAsState()
                    AuthNavigation(uiState = uiState, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AuthNavigation(
    uiState: AuthUiState,
    viewModel: AuthViewModel
) {
    // Crossfade handles smooth transitions between states
    Crossfade(
        targetState = uiState,
        label = "AuthNavigation",
        animationSpec = tween(durationMillis = 300)
    ) { state ->
        when (state) {
            is AuthUiState.Login -> {
                LoginScreen(
                    onSendOtpClick = { email -> viewModel.sendOtp(email) }
                )
            }
            is AuthUiState.Loading -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }
            is AuthUiState.OtpSent -> {
                OtpScreen(
                    email = state.email,
                    error = state.error,
                    onVerifyClick = { code -> viewModel.verifyOtp(state.email, code) },
                    onResendClick = { viewModel.resendOtp(state.email) },
                    onClearError = { viewModel.clearError() }
                )
            }
            is AuthUiState.LoggedIn -> {
                SessionScreen(
                    email = state.email,
                    sessionStartTime = state.sessionStartTime,
                    durationSeconds = state.durationSeconds,
                    onLogoutClick = { viewModel.logout() }
                )
            }
        }
    }
}
