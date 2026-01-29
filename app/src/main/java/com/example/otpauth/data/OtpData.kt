package com.example.otpauth.data

/**
 * Data class representing an OTP entry.
 * @param code The 6-digit OTP code.
 * @param expiryTime The timestamp (ms) when the OTP expires.
 * @param attempts The number of attempts made for this OTP.
 */
data class OtpData(
    val code: String,
    val expiryTime: Long,
    var attempts: Int = 0
)
