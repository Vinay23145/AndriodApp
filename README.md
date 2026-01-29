# Android OTP Authentication App

## Overview
A production-quality Android application demonstrating a passwordless **Email + OTP** authentication flow. Built with **Kotlin** and **Jetpack Compose**, following modern Android architectural patterns (MVVM + UDF).

This project was built to demonstrate clean code, correct state management, and robustness in handling edge cases like screen rotation and process death.

## 🏗️ Tech Stack

*   **Language:** Kotlin
*   **UI:** Jetpack Compose (Material3)
*   **Architecture:** MVVM (Model-View-ViewModel) + UDF (Unidirectional Data Flow)
*   **Concurrency:** Kotlin Coroutines & Flow
*   **Logging/Analytics:** Timber (simulating Analytics)
*   **Build System:** Gradle (Kotlin DSL) + Version Catalog

## 🧩 Architecture Decisions

### UI Layer
The UI is built using **Jetpack Compose**.
*   **Single Activity:** `MainActivity` hosts the navigation logic.
*   **State Hoisting:** All screens (`LoginScreen`, `OtpScreen`, `SessionScreen`) are stateless and receive state/callbacks from `AuthNavigation` (which observes the ViewModel).
*   **State Management:** `AuthUiState` is a sealed interface representing the exhaustive states of the screen (`Login`, `Loading`, `OtpSent`, `LoggedIn`). This ensures the UI always renders a valid state.

### Data Layer
*   **OtpManager:** A Singleton object acting as the Data Source. It handles:
    *   OTP Generation (Random 6 digits).
    *   Storage (In-memory `Map<String, OtpData>`).
    *   Validation Logic (Expiry check, Attempt limits, Code matching).
    *   *Note: In a fully scaled app, this would be a Repository backed by a Remote Data Source (API).*

### Analytics
*   **Timber:** Selected for its simplicity and ubiquity in the Android ecosystem.
*   **AnalyticsLogger:** A wrapper object around Timber to simulate structured event logging (e.g., `OTP_Generated`, `Logout`). This abstraction allows easily swapping the underlying implementation (e.g., to Firebase) without changing call sites.

## ⚡ Key Features & Logic

### 1. OTP Handling
*   **Generation:** A cryptographically secure random number is not strictly necessary for this demo, so standard `Random` is used for 6 digits.
*   **Expiry:** OTPs are valid for **60 seconds**. Validation checks `System.currentTimeMillis() > expiryTime`.
*   **Storage:** Stored in `OtpManager` keyed by Email. Generating a new OTP for the same email overwrites the previous one (invalidating it).
*   **Attempts:** Max **3 attempts**. After 3 failed tries, the OTP is invalidated for security.

### 2. Session Management
*   **Timer:** Managed by `AuthViewModel` using a Coroutine loop (`while(true) delay(1000)`).
*   **Process Death:** The implementation focuses on in-memory state. For full process death survival, `SavedStateHandle` would be integrated, but `rememberSaveable` handles configuration changes (rotation) gracefully for local UI state. ViewModel survives rotation, keeping the timer running.

## 🤖 AI Assistance vs. Human Engineering

*   **Architecture & Structure:** Human-designed. The decision to use a sealed `AuthUiState` and a dedicated `OtpManager` was made to ensure separation of concerns and testability.
*   **Boilerplate Generation:** AI was used to generate standard Compose boilerplate (Scaffolds, TextFields) and Gradle build files to speed up setup.
*   **Logic Verification:** AI assisted in double-checking the edge case logic (e.g., ensure timer stops on logout).

## 🚀 How to Run

1.  Open the project in **Android Studio**.
2.  Sync Gradle.
3.  Run on an Emulator or Physical Device.
4.  **Testing Flow:**
    *   Enter a valid email.
    *   Check **Logcat** (filter for "OTP") to see the generated code.
    *   Enter the code to login.
    *   Watch the session timer.
    *   Rotate the device to verify state persistence.
