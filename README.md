# 🔐 Android Passwordless Authentication App (Email + OTP)

## 📌 Overview
This project demonstrates a **passwordless authentication flow** using **Email + OTP**, followed by a **session tracking screen**.  
All OTP logic is implemented **locally**, with **Firebase SDK** used only for logging and analytics, as required by the assignment.

🎥 A complete working demo is provided in the uploaded video.

---

## 🛠 Tech Stack
- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture:** MVVM (ViewModel + UI State)  
- **Async:** Kotlin Coroutines  
- **SDK:** Firebase (Analytics / Logging)  
- **IDE:** Android Studio  

---

## 🔢 OTP Logic (Core Requirement)

### OTP Generation
- Implemented via `generateOtp`
- **6-digit numeric OTP**, generated locally
- OTP stored **per email**
- Regenerating OTP:
  - Invalidates previous OTP
  - Resets attempt count

### OTP Verification
- Implemented via `verifyOtp`
- Validation includes:
  - OTP match check
  - **60-second expiry**
  - **Maximum 3 attempts**

### Attempt & Resend Handling
- Only **3 attempts** allowed
- After limit exceeded:
  - OTP becomes invalid
  - Login is blocked
  - User must **resend OTP**
- Resend generates a fresh OTP and resets attempts

---

## ⏱ OTP Rules
- **Length:** 6 digits  
- **Expiry:** 60 seconds  
- **Attempts:** Max 3  

✔ Enforced fully **locally** (no backend).

---

## 🗂 Data Structures
- `Map<String, OtpData>`
  - **Key:** Email  
  - **Value:** OTP, timestamp, attempt count  

**Reason:** Fast lookup, clean per-user state, clear separation of logic.

---

## 🕒 Session Screen
After successful login:
- Shows **session start time**
- Displays **live duration (mm:ss)**
- Logout ends the session

**Timer behavior:**
- Uses **Coroutines**
- Survives recompositions & screen rotation
- Stops correctly on logout

---

## 📊 External SDK (Firebase)
Firebase SDK is properly integrated for **event logging only**.

**Logged events:**
- OTP generated  
- OTP validation success  
- OTP validation failure  
- Logout  

---

## 🧠 Architecture & Compose
- One-way data flow
- ViewModel handles business logic
- UI observes immutable state
- No UI logic in ViewModel
- No global mutable state

**Compose APIs used:**  
`@Composable`, `remember`, `rememberSaveable`, `LaunchedEffect`, state hoisting

---

## ⚠️ Edge Cases Covered
- Expired OTP  
- Incorrect OTP  
- Attempt limit exceeded  
- Resend OTP flow  
- Screen rotation without state loss  
- Proper logout cleanup  

---

## 🤖 GPT Usage
GPT was used only for **concept clarification and best practices**.  
All OTP logic, session handling, and Firebase integration were **understood and implemented manually**.

---
## ▶️ Setup
```bash
git clone https://github.com/Vinay23145/AndriodApp.git
