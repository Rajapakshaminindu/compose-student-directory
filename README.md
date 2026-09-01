# Student Directory Manager

A modern Android application for managing student information with real-time synchronization using Firebase Firestore. Built with Jetpack Compose and Material Design 3.

## Features

- **📱 Modern UI**: Built with Jetpack Compose and Material Design 3
- **🔄 Real-time Sync**: Firebase Firestore integration for instant data synchronization
- **➕ Add Students**: Create new student records with a user-friendly dialog
- **✏️ Edit Students**: Update existing student information seamlessly
- **🗑️ Delete Students**: Remove student records from the database
- **👁️ View Students**: Browse all students in a clean, scrollable list
- **🔐 Cloud Backend**: Secure data storage with Firebase

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Backend**: Firebase Firestore
- **Minimum SDK**: 24
- **Target SDK**: 37
- **Build System**: Gradle (Kotlin DSL)

## Project Structure

```
Activity_03/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/activity_03/
│   │       │   ├── MainActivity.kt          # Main activity with UI
│   │       │   ├── model/
│   │       │   │   └── Student.kt           # Data model
│   │       │   ├── viewmodel/
│   │       │   │   └── StudentViewModel.kt  # Business logic
│   │       │   └── ui/
│   │       ├── AndroidManifest.xml
│   │       └── res/
│   ├── build.gradle.kts                     # App dependencies
│   └── google-services.json                 # Firebase configuration
├── viewmodel/
│   └── StudentViewModel.kt
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/

```

## Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Firebase account with Firestore enabled
- Kotlin 1.8 or higher

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Activity_03
   ```

2. **Configure Firebase**
   - Download your `google-services.json` file from Firebase Console
   - Place it in the `app/` directory
   - Ensure Firestore database is created and accessible

3. **Build and Run**
   ```bash
   ./gradlew build
   ./gradlew installDebug
   ```
   Or use Android Studio's Run button

4. **Firebase Firestore Setup**
   - Create a collection named `students` in your Firestore database
   - Set appropriate security rules for read/write access

## Usage

1. **Launch the app** on your Android device or emulator
2. **View Students**: The main screen displays all students from Firestore
3. **Add a Student**: Tap the floating action button (+) to add a new student
4. **Edit a Student**: Tap the edit icon next to a student's entry
5. **Delete a Student**: Tap the delete icon to remove a student

## Key Components

### StudentViewModel
Manages all business logic for student operations:
- Real-time listener for Firestore changes
- CRUD operations (Create, Read, Update, Delete)
- StateFlow for reactive UI updates

### Student Model
Data class representing a student entity with Firestore serialization

### UI Screens
- **StudentDirectoryScreen**: Main screen with student list and action buttons
- **Add/Edit Dialog**: Modal for creating or updating student information

## Dependencies

- **Jetpack Compose**: UI framework
- **Firebase Firestore**: Real-time database
- **Kotlin Coroutines**: Asynchronous programming
- **Lifecycle**: ViewModel and state management
- **Material Icons**: Icon library

## API Reference

### StudentViewModel Methods

```kotlin
fun addStudent(student: Student)           // Add a new student
fun updateStudent(student: Student)        // Update existing student
fun deleteStudent(studentId: String)       // Delete a student
```

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## Future Enhancements

- [ ] Student search functionality
- [ ] Student filtering by class/grade
- [ ] Export student data to CSV
- [ ] Student profile pictures
- [ ] Offline sync capability
- [ ] Student attendance tracking
- [ ] Grade management system

## License

This project is open source and available under the MIT License.

## Contact

For questions or suggestions, please reach out or open an issue in the repository.

---

**Repository Suggestions:**

### Option 1 (Recommended):
- **Name**: `StudentDirectoryApp`
- **Description**: A modern Android app for managing student information with real-time Firebase Firestore sync, built with Jetpack Compose and Material Design 3.

### Option 2:
- **Name**: `StudentManager`
- **Description**: Real-time student management application with Firebase backend, featuring modern Android UI built with Jetpack Compose.

### Option 3:
- **Name**: `compose-student-directory`
- **Description**: Android student directory application showcasing Jetpack Compose, Firebase Firestore integration, and MVVM architecture patterns.
