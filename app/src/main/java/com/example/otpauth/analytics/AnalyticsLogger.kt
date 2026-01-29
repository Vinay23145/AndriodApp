package com.example.otpauth.analytics

import timber.log.Timber

/**
 * Singleton object to handle application analytics.
 * Currently uses Timber for logging, but abstracts the implementation details
 * allowing for easy swap to Firebase or Sentry in the future.
 */
object AnalyticsLogger {

    fun init() {
        // Timber.plant is called in Application class for Debug builds.
        // For release builds, we might plant a Crashlytics tree here.
        logEvent("Analytics Initialized")
    }

    fun logEvent(event: String, params: Map<String, Any>? = null) {
        if (params != null) {
            Timber.i("Event: $event, Params: $params")
        } else {
            Timber.i("Event: $event")
        }
    }

    fun logOtpGenerated(email: String) {
        logEvent("OTP_Generated", mapOf("email" to email))
    }

    fun logOtpValidationSuccess(email: String) {
        logEvent("OTP_Validation_Success", mapOf("email" to email))
    }

    fun logOtpValidationFailure(email: String, reason: String) {
        logEvent("OTP_Validation_Failure", mapOf("email" to email, "reason" to reason))
    }

    fun logLogout(durationSeconds: Long) {
        logEvent("Logout", mapOf("session_duration" to durationSeconds))
    }
}
