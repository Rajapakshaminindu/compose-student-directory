# Contributing to Student Directory Manager

Thank you for your interest in contributing to **Student Directory Manager**! This project is an open-source Android application built with Jetpack Compose, Kotlin, and Firebase Firestore.

We welcome contributions of all kinds: bug fixes, feature requests, UI enhancements, documentation improvements, and architectural suggestions.

---

## Code of Conduct

By participating in this project, you agree to abide by our [Code of Conduct](CODE_OF_CONDUCT.md). Please treat all contributors with respect and kindness.

---

## How Can I Contribute?

### 1. Reporting Bugs
- Check the [existing issues](https://github.com/Rajapakshaminindu/compose-student-directory/issues) to ensure your bug hasn't already been reported.
- If it's a new bug, create a new issue using our **Bug Report** template.
- Provide a clear title, reproduction steps, expected behavior, device/Android version, and relevant logcat output.

### 2. Suggesting Features
- Open an issue using the **Feature Request** template.
- Describe the feature, its motivation, and any UI mockups or implementation ideas you have.

### 3. Submitting Code (Pull Requests)
1. **Fork** the repository to your own GitHub account.
2. **Clone** your fork locally:
   ```bash
   git clone https://github.com/<your-username>/compose-student-directory.git
   cd compose-student-directory
   ```
3. Create a descriptive topic branch:
   ```bash
   git checkout -b feature/attendance-tracker
   # or
   git checkout -b fix/search-filter-case
   ```
4. Implement your changes following our coding style and guidelines.
5. Run tests locally:
   ```bash
   ./gradlew test
   ```
6. Commit using [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat: add student sorting options`
   - `fix: resolve crash on null phone number`
   - `docs: update setup steps in README`
   - `test: add unit tests for repository layer`
7. Push to your fork and submit a **Pull Request** against the `master` branch.

---

## Coding Standards

- **Kotlin Style Guide**: Follow the standard [Android Kotlin style guidelines](https://developer.android.com/kotlin/style-guide).
- **Compose Best Practices**:
  - Keep composables stateless where possible (state hoisting).
  - Use `Modifier` as the first optional parameter in reusable composables.
  - Utilize Material 3 theme tokens (`MaterialTheme.colorScheme`, `MaterialTheme.typography`).
- **Architecture**:
  - Maintain the MVVM pattern with unidirectional data flow (UDF).
  - UI observes `StateFlow` from ViewModels; UI never directly talks to Firestore.
- **Testing**:
  - Add unit tests for any new data models, validators, or repository classes.

---

## Questions?

Feel free to open a discussion or issue in the GitHub repository. Thank you for making Student Directory Manager better!
