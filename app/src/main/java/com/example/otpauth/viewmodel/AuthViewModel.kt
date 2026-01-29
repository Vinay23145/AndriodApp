package com.example.otpauth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otpauth.analytics.AnalyticsLogger
import com.example.otpauth.data.OtpManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ViewModel to handle business logic for Authentication and Session management.
 * Follows the UDF (Unidirectional Data Flow) pattern.
 */
class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Login)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var sessionTimerJob: Job? = null

    /**
     * Sends OTP to the provided email.
     */
    fun sendOtp(email: String) {
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             Timber.w("Invalid email provided")
             return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            // Perform OTP generation on IO dispatcher to avoid blocking Main thread
            val otpCode = withContext(Dispatchers.IO) {
                // Simulate network delay
                delay(500)
                OtpManager.generateOtp(email)
            }
            
            Timber.i("OTP Generated for $email: $otpCode")
            println("OTP Generated for $email: $otpCode")

            _uiState.value = AuthUiState.OtpSent(email = email)
        }
    }

    /**
     * Validates the entered OTP.
     */
    fun verifyOtp(email: String, code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            val result = withContext(Dispatchers.IO) {
                OtpManager.validateOtp(email, code)
            }
            
            when (result) {
                is OtpManager.ValidationResult.Success -> {
                    startSession(email)
                }
                is OtpManager.ValidationResult.Failure -> {
                    _uiState.value = AuthUiState.OtpSent(email = email, error = result.reason)
                }
            }
        }
    }

    /**
     * Resends OTP.
     */
    fun resendOtp(email: String) {
        sendOtp(email)
    }

    /**
     * Starts the session timer.
     */
    private fun startSession(email: String) {
        val startTime = System.currentTimeMillis()
        _uiState.value = AuthUiState.LoggedIn(
            email = email,
            sessionStartTime = startTime,
            durationSeconds = 0
        )

        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val duration = (System.currentTimeMillis() - startTime) / 1000
                _uiState.update {
                    if (it is AuthUiState.LoggedIn) {
                        it.copy(durationSeconds = duration)
                    } else {
                        it
                    }
                }
            }
        }
    }

    /**
     * Logs out the user and stops the timer.
     */
    fun logout() {
        val currentState = _uiState.value
        if (currentState is AuthUiState.LoggedIn) {
            AnalyticsLogger.logLogout(currentState.durationSeconds)
        }
        
        sessionTimerJob?.cancel()
        _uiState.value = AuthUiState.Login
    }
    
    fun clearError() {
         _uiState.update {
            if (it is AuthUiState.OtpSent) {
                it.copy(error = null)
            } else {
                it
            }
        }
    }
}
