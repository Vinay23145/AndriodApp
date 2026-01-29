# Android OTP Authentication App

## Project Overview
This Android application implements a **passwordless Email + OTP authentication system** using **Kotlin** and **Jetpack Compose**.  
The project follows modern Android development practices and is designed to be stable, readable, and recruiter-friendly.

This application demonstrates:
- OTP generation and validation
- Proper expiry and retry handling
- Safe background processing
- Clean UI state management
- Debug-friendly local OTP verification

---

## 1. OTP Logic and Expiry Handling

### OTP Generation
- A **6-digit numeric OTP** is generated when the user clicks **Send OTP**.
- OTP is generated per email ID.
- Generating a new OTP invalidates the previous OTP for that email.

### OTP Expiry
- OTP validity: **60 seconds**
- Expiry is calculated using:

- If the OTP is expired, verification fails and the user must request a new OTP.

### Attempt Limit
- Maximum **3 attempts** are allowed.
- After 3 incorrect attempts:
- OTP becomes invalid
- User must request a new OTP

### Thread Safety
- OTP generation and verification run on **Dispatchers.IO**.
- Prevents UI freezing and ANR issues.

---

## 2. Data Structures Used and Why

### ConcurrentHashMap<String, OtpData>
- Stores OTP details mapped to email.
- Thread-safe and safe for background execution.
- Prevents race conditions.

### OtpData (Data Class)
Stores:
- OTP value
- Expiry time
- Attempt count

This keeps OTP handling structured and easy to maintain.

### AuthUiState (Sealed Interface)
Represents UI states:
- Login
- Loading
- OTP Sent
- Logged In

This ensures:
- Safe UI rendering
- No invalid UI states
- Better readability and maintainability

---

## 3. External SDKs / Libraries Used

### Jetpack Compose (Material 3)
- Modern declarative UI
- Less boilerplate
- Recommended by Google

### Kotlin Coroutines & StateFlow
- Background execution
- UI-safe state updates
- Prevents ANR issues

### ViewModel
- Preserves state across screen rotation
- Separates UI and business logic

### Compose BOM (Bill of Materials)
- Ensures all Compose dependencies are version-aligned
- Prevents runtime crashes caused by binary incompatibility

### Timber (Analytics Simulation)
- Lightweight logging
- Easy to replace with Firebase Analytics

---

## 4. GPT Usage vs Human Implementation

### GPT Used For
- Generating Compose UI boilerplate
- Gradle dependency setup
- Debugging Compose version conflicts
- Suggesting performance fixes

### Human Implemented and Understood
- OTP logic
- Expiry and attempt handling
- Threading decisions
- ANR and crash fixes using Logcat
- Project structure and Git workflow

All logic was reviewed, tested, and understood before final implementation.

---

## How to Run the Project

1. Open the project in **Android Studio**
2. Sync Gradle
3. Run on emulator or physical device
4. Enter an email and click **Send OTP**
5. Check **Logcat** (filter: `OTP`) to see the OTP (local testing)
6. Enter OTP and verify login
7. Session screen will appear
8. Rotate screen to verify state persistence

---

## Notes
- This project uses **local OTP logging** for testing.
- No external SMS or Email API is required.
- Designed to be easily extendable to real backend OTP services.
