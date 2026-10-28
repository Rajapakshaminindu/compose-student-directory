# System Architecture

This document describes the architectural principles, layers, and data flow of the **Student Directory Manager** application.

---

## High-Level Architecture (MVVM)

The application follows the official Android Architecture Guidelines with a reactive, unidirectional data flow (UDF):

```
┌───────────────────────────────────────────────┐
│              UI Layer (Compose)               │
│  - StudentDirectoryScreen                     │
│  - StatisticsCard, FilterChipRow, Dialogs     │
└───────────────────────▲───────────────────────┘
                        │ Observes StateFlow<UiState>
                        │ Dispatches User Intents (events)
┌───────────────────────▼───────────────────────┐
│               ViewModel Layer                 │
│  - StudentViewModel                           │
│  - StateFlow streams & Coroutine Scopes       │
└───────────────────────▲───────────────────────┘
                        │
                        │ Calls Suspend / Flows
┌───────────────────────▼───────────────────────┐
│               Data / Repository Layer         │
│  - StudentRepository (Interface)              │
│  - FirestoreStudentRepository                 │
└───────────────────────▲───────────────────────┘
                        │
                        │ Real-time Snapshots
┌───────────────────────▼───────────────────────┐
│               Cloud Firestore                 │
│  - Collection: "students"                     │
└───────────────────────────────────────────────┘
```

---

## Layer Responsibilities

### 1. UI Layer (Jetpack Compose)
- Stateless Composables receiving data and emitting callbacks (`onClick`, `onQueryChanged`).
- Uses Material 3 design system tokens (`MaterialTheme.colorScheme`, `MaterialTheme.typography`).
- Observes `StateFlow` via `collectAsStateWithLifecycle()` to remain lifecycle-aware.

### 2. ViewModel Layer
- Survives configuration changes (e.g. screen rotations).
- Manages business logic, search filters, error reporting, and loading indicators.
- Emits immutable `StateFlow` states for UI rendering.

### 3. Data / Repository Layer
- Exposes clean Kotlin coroutines `suspend` functions and `Flow<List<Student>>`.
- Abstracts network and database details away from UI logic.
- Converts raw Firestore documents into validated Kotlin data classes.

---

## State Flow Diagram

```
User Action (e.g. Add Student)
         │
         ▼
StudentDirectoryScreen dispatches viewModel.addStudent(student)
         │
         ▼
StudentViewModel executes viewModelScope.launch
         │
         ▼
Firestore writes document to "students" collection
         │
         ▼
Firestore triggers Snapshot Listener
         │
         ▼
StudentViewModel receives updated snapshot & updates _students.value
         │
         ▼
StudentDirectoryScreen recomposes automatically
```
