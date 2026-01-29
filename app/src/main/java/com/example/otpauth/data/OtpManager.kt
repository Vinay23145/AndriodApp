package com.example.otpauth.data

import com.example.otpauth.analytics.AnalyticsLogger
import timber.log.Timber
import kotlin.random.Random
import java.util.concurrent.ConcurrentHashMap

/**
 * Singleton to manage OTP generation, storage, and validation.
 * In a real app, this would likely be a Repository interfacing with a Backend.
 */
object OtpManager {

    private const val OTP_LENGTH = 6
    private const val OTP_EXPIRY_MS = 60_000L // 60 seconds
    private const val MAX_ATTEMPTS = 3

    // In-memory storage: Email -> OtpData
    private val otpStorage = ConcurrentHashMap<String, OtpData>()

    /**
     * Generates a new OTP for the given email.
     * Invalidates any previous OTP for this email.
     * @return The generated OTP code.
     */
    fun generateOtp(email: String): String {
        val otpCode = (1..OTP_LENGTH)
            .map { Random.nextInt(0, 10) }
            .joinToString("")

        val expiryTime = System.currentTimeMillis() + OTP_EXPIRY_MS
        otpStorage[email] = OtpData(code = otpCode, expiryTime = expiryTime)
        
        AnalyticsLogger.logOtpGenerated(email)

        // Ensure OTP is logged for testing in debug builds
        Timber.d("DEBUG OTP for $email: $otpCode")

        return otpCode
    }

    sealed class ValidationResult {
        object Success : ValidationResult()
        data class Failure(val reason: String) : ValidationResult()
    }

    /**
     * Validates the OTP for the given email.
     */
    fun validateOtp(email: String, code: String): ValidationResult {
        val otpData = otpStorage[email]

        if (otpData == null) {
            AnalyticsLogger.logOtpValidationFailure(email, "No OTP found")
            return ValidationResult.Failure("No OTP request found for this email.")
        }

        if (System.currentTimeMillis() > otpData.expiryTime) {
            otpStorage.remove(email) // Cleanup expired
            AnalyticsLogger.logOtpValidationFailure(email, "Expired")
            return ValidationResult.Failure("OTP has expired. Please request a new one.")
        }

        if (otpData.attempts >= MAX_ATTEMPTS) {
            otpStorage.remove(email) // Security measure
            AnalyticsLogger.logOtpValidationFailure(email, "Max attempts exceeded")
            return ValidationResult.Failure("Too many failed attempts. OTP invalidated.")
        }

        if (otpData.code != code) {
            otpData.attempts++
            AnalyticsLogger.logOtpValidationFailure(email, "Incorrect code")
            return ValidationResult.Failure("Incorrect OTP. Attempts left: ${MAX_ATTEMPTS - otpData.attempts}")
        }

        // Success
        otpStorage.remove(email) // Consume OTP
        AnalyticsLogger.logOtpValidationSuccess(email)
        return ValidationResult.Success
    }
}
