package com.example.otpauth.viewmodel

/**
 * Represents the various states of the Authentication UI.
 * This is a sealed interface to allow for exhaustive `when` statements in the UI.
 */
sealed interface AuthUiState {
    // Initial state: User needs to enter email
    object Login : AuthUiState

    // Loading state: Waiting for OTP generation or validation
    object Loading : AuthUiState

    // OTP Sent state: User needs to enter OTP
    data class OtpSent(
        val email: String,
        val error: String? = null // To display errors like "Incorrect OTP"
    ) : AuthUiState

    // Logged In state: Session active
    data class LoggedIn(
        val email: String,
        val sessionStartTime: Long,
        val durationSeconds: Long
    ) : AuthUiState
}
