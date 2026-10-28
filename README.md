# Student Directory Manager

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%20Design-3-blue.svg?style=flat&logo=materialdesign)](https://m3.material.io)
[![Firebase Firestore](https://img.shields.io/badge/Firebase-Firestore-orange.svg?style=flat&logo=firebase)](https://firebase.google.com/docs/firestore)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A modern Android application for managing student records with real-time cloud synchronization using Firebase Firestore. Designed and crafted using Jetpack Compose and Material Design 3 following MVVM architectural best practices.

---

## 📸 Overview

The **Student Directory Manager** provides a clean, responsive UI to maintain student profiles seamlessly across devices. Any changes made to the database reflect instantly in the app via Firestore snapshots.

### ✨ Highlights
- **Modern Jetpack Compose UI**: Built with 100% declarative UI and Material You design tokens.
- **Instant Search & Filter**: Real-time filtering across name, roll number, class/grade, and email.
- **Dynamic GPA Indicators**: Visual status indicators dynamically styled according to academic performance.
- **Real-Time Data Sync**: Firebase Firestore snapshot listeners ensure instant synchronization without manual reloads.
- **Complete CRUD Functionality**: Create, Read, Update, and Delete student profiles with dialog workflows.
- **Dark & Light Mode**: Comprehensive theming with support for Android 12+ dynamic wallpaper colors.

---

## 🛠 Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM (Model - View - ViewModel) + unidirectional data flow (UDF)
- **Reactive State**: Kotlin Coroutines & `StateFlow`
- **Backend / Cloud**: [Firebase Firestore](https://firebase.google.com/docs/firestore)
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)

---

## 📁 Project Structure

```
compose-student-directory/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/activity_03/
│   │   │   │   ├── MainActivity.kt                  # Activity entry point with Edge-to-Edge
│   │   │   │   ├── model/
│   │   │   │   │   └── Student.kt                   # Firestore entity & mapper functions
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── StudentViewModel.kt          # Business logic & StateFlow streams
│   │   │   │   └── ui/
│   │   │   │       ├── StudentDirectoryScreen.kt    # Main directory UI, cards & dialogs
│   │   │   │       └── theme/
│   │   │   │           ├── Theme.kt                 # Material 3 dark/light/dynamic color schemes
│   │   │   │           └── Type.kt                  # Material 3 typography definitions
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   │       └── java/com/example/activity_03/
│   │           └── model/
│   │               └── StudentTest.kt               # Unit tests for data serialization & logic
│   └── build.gradle.kts                             # Module configuration & dependencies
├── build.gradle.kts                                 # Project build configuration
├── settings.gradle.kts                              # Gradle repository & module inclusion
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio Hedgehog](https://developer.android.com/studio) or newer
- JDK 17
- Android SDK 24+
- A Google Firebase account with Cloud Firestore enabled

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Rajapakshaminindu/compose-student-directory.git
   cd compose-student-directory
   ```

2. **Add Firebase Credentials**
   - Head over to the [Firebase Console](https://console.firebase.google.com/) and create a project.
   - Register an Android app using the package name `com.example.activity_03`.
   - Download `google-services.json` and place it in the `app/` directory:
     ```
     app/google-services.json
     ```
   - In Firestore Database, create a collection named `students` or let the app automatically initialize it on first entry.

3. **Configure Firestore Rules (Development Mode)**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /students/{studentId} {
         allow read, write: if true;
       }
     }
   }
   ```

4. **Build & Run**
   Open the project in Android Studio, sync Gradle, and run on an emulator or physical device.

---

## 🧪 Testing

Run the included local unit tests via Gradle:
```bash
./gradlew test
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is open-source and distributed under the [MIT License](LICENSE).
