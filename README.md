# Student Directory Manager

[![Android CI](https://github.com/Rajapakshaminindu/compose-student-directory/actions/workflows/android.yml/badge.svg)](https://github.com/Rajapakshaminindu/compose-student-directory/actions/workflows/android.yml)
[![Lint & Code Style](https://github.com/Rajapakshaminindu/compose-student-directory/actions/workflows/lint.yml/badge.svg)](https://github.com/Rajapakshaminindu/compose-student-directory/actions/workflows/lint.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-purple.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.02.00-green.svg?style=flat&logo=android)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%20Design-3-blue.svg?style=flat&logo=materialdesign)](https://m3.material.io)
[![Firebase Firestore](https://img.shields.io/badge/Firebase-Firestore-orange.svg?style=flat&logo=firebase)](https://firebase.google.com/docs/firestore)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

A modern, production-ready Android application for managing student profiles and academic records with real-time cloud synchronization using Firebase Firestore. Crafted with Jetpack Compose and Material Design 3 following MVVM architecture and clean code best practices.

---

## 📸 Highlights & Features

- **📱 Modern Jetpack Compose UI**: 100% declarative UI built with Material Design 3 and Edge-to-Edge display support.
- **🔄 Real-time Firestore Sync**: Automatic instant synchronization across all active devices via Snapshot listeners.
- **🔍 Instant Search & Filter**: Real-time multi-field search (by Name, Student ID, Grade, Email) and quick Grade FilterChips.
- **📊 Aggregate Dashboard Card**: Live statistics tracking total enrolled students, average GPA, and honors distinctions.
- **🏷️ Dynamic GPA Badges**: Color-coded academic indicators (Distinction, Average, At-Risk).
- **📝 Complete CRUD Operations**: Add, Edit, Delete, and Detailed View dialogs with built-in input validation.
- **📤 CSV Export**: Export directory records to RFC 4180 compliant CSV format for offline reporting.
- **🌓 Dynamic Theming**: Complete Dark/Light mode support with Android 12+ wallpaper dynamic colors.
- **🛡️ Secure Cloud Storage**: Includes Firestore Security Rules for data integrity.

---

## 🛠 Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM (Model - View - ViewModel) with Repository Pattern (see [ARCHITECTURE.md](ARCHITECTURE.md))
- **Asynchronous & Reactive**: Kotlin Coroutines & `StateFlow`
- **Backend / Database**: [Google Cloud Firebase Firestore](https://firebase.google.com/docs/firestore)
- **Continuous Integration**: GitHub Actions CI for automated builds, unit tests, and lint checks
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 34 (Android 14)

---

## 📁 Project Structure

```
compose-student-directory/
├── .github/
│   ├── workflows/
│   │   ├── android.yml              # CI automated build & test pipeline
│   │   └── lint.yml                 # Code quality & linting checks
│   ├── ISSUE_TEMPLATE/              # Bug report and Feature request templates
│   └── pull_request_template.md     # Standard PR checklist
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── assets/sample_students.json          # Mock dataset for offline testing
│   │   │   ├── java/com/example/activity_03/
│   │   │   │   ├── MainActivity.kt                  # Activity entry point with Edge-to-Edge
│   │   │   │   ├── model/
│   │   │   │   │   ├── Student.kt                   # Student entity with Firestore mappings
│   │   │   │   │   └── AcademicRecord.kt            # Course grades & attendance models
│   │   │   │   ├── repository/
│   │   │   │   │   └── StudentRepository.kt         # Clean architecture repository layer
│   │   │   │   ├── viewmodel/
│   │   │   │   │   └── StudentViewModel.kt          # Business logic & reactive state
│   │   │   │   ├── ui/
│   │   │   │   │   ├── StudentDirectoryScreen.kt    # Root directory UI screen
│   │   │   │   │   ├── components/                  # Reusable UI components
│   │   │   │   │   │   ├── StatisticsCard.kt        # Aggregate metrics card
│   │   │   │   │   │   ├── StudentDetailDialog.kt   # Detailed profile viewer
│   │   │   │   │   │   ├── FilterChipRow.kt         # Grade category filter row
│   │   │   │   │   │   └── SortDropdown.kt          # Sorting options menu
│   │   │   │   │   └── theme/                       # M3 Color scheme & Typography
│   │   │   │   └── util/
│   │   │   │       ├── Validators.kt                # Input validation engine
│   │   │   │       └── CsvExportUtil.kt             # CSV export utility
│   │   │   └── AndroidManifest.xml
│   │   └── test/                                    # Unit test suite
│   │       └── java/com/example/activity_03/
│   │           ├── model/StudentTest.kt
│   │           ├── model/AcademicRecordTest.kt
│   │           └── util/ValidatorsTest.kt
│   └── build.gradle.kts
├── firestore.rules                                  # Cloud Firestore security & validation rules
├── ARCHITECTURE.md                                  # Architectural documentation & flowcharts
├── CONTRIBUTING.md                                  # Contribution guidelines
├── CODE_OF_CONDUCT.md                               # Contributor Code of Conduct
├── LICENSE                                          # MIT License
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio Hedgehog](https://developer.android.com/studio) or newer
- JDK 17
- Android SDK 24+
- A Google Firebase account with Cloud Firestore enabled

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/Rajapakshaminindu/compose-student-directory.git
   cd compose-student-directory
   ```

2. **Add Firebase Credentials**
   - Head over to the [Firebase Console](https://console.firebase.google.com/) and create a project.
   - Register an Android app using package: `com.example.activity_03`.
   - Download `google-services.json` and place it in the `app/` directory:
     ```
     app/google-services.json
     ```
   - In Firestore Database, create a collection named `students` or use [firestore.rules](firestore.rules) to configure security rules.

3. **Build & Run**
   Open the project in Android Studio, allow Gradle to sync dependencies, and hit **Run**.

---

## 🧪 Testing

Execute the automated unit test suite using Gradle:
```bash
./gradlew test
```

---

## 🤝 Contributing

Contributions, issues, and feature suggestions are welcome! Please check our:
- [Contributing Guidelines](CONTRIBUTING.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)
- [Architecture Guide](ARCHITECTURE.md)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
