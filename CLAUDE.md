# ActivityLauncher Developer Guide

## Build & Test Commands
- Build: `./gradlew build`
- Clean: `./gradlew clean`
- Install Debug: `./gradlew installDebug`
- Run Lint: `./gradlew lint`
- Run Tests: `./gradlew test`
- Run Single Test: `./gradlew :app:testDebugUnitTest --tests "de.szalkowski.activitylauncher.TestClassName.testMethodName"`

## Code Style Guidelines
- **Kotlin Style**: Follow Kotlin coding conventions from kotlinlang.org
- **Imports**: Group by package, no wildcards, alphabetically ordered
- **Naming**:
  - Classes: PascalCase
  - Functions/Variables: camelCase
  - Constants: UPPER_SNAKE_CASE
- **DI**: Use Hilt for dependency injection
- **UI**: Use ViewBinding for view references
- **Architecture**: Services for business logic, UI for presentation
- **Error Handling**: Use nullable types or Result pattern, avoid uncaught exceptions
- **Navigation**: Use Android Navigation Component

## Project Structure
- `services/`: Business logic and data operations
- `ui/`: UI components and fragments