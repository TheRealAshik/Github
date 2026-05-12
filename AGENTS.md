# AGENTS.md

Guidelines for AI agents and contributors working on this codebase.

## Architecture

This is a **Kotlin Multiplatform (KMP)** project with **Compose Multiplatform** UI.

```
composeApp/src/
├── commonMain/   # All shared UI (Compose) and business logic
├── androidMain/  # Android entry point & platform APIs
└── iosMain/      # iOS entry point & platform APIs
```

- **Single module** (`composeApp`) — no feature modules yet.
- **No backend** — all data comes from the GitHub REST/GraphQL API.
- Shared ViewModels live in `commonMain` using `androidx.lifecycle.viewmodel`.
- Platform-specific code is injected via `expect/actual`.

## Navigation

- Bottom nav: **Home · Inbox · Explore · Copilot**
- Each tab owns its own back stack.
- Screens are plain `@Composable` functions; no Fragment or Activity per screen.

## Code Quality Rules

### General
- Write Kotlin idiomatically — prefer `val`, data classes, sealed classes, and extension functions.
- No business logic in `@Composable` functions. Keep composables dumb; logic goes in ViewModels.
- One responsibility per file. Split large files before they exceed ~300 lines.

### Naming
- Screens: `XxxScreen.kt` (e.g. `HomeScreen.kt`)
- ViewModels: `XxxViewModel.kt`
- UI components: `XxxComponent.kt` or descriptive name in a `components/` package
- State holders: `XxxUiState` (sealed class or data class)

### Compose
- Hoist state up; composables receive state + lambdas, never call ViewModel directly.
- Use `remember` / `derivedStateOf` to avoid unnecessary recompositions.
- Preview every reusable component with `@Preview`.

### UI Design
- Follow **Material 3 Expressive** design guidelines — use expressive color roles, dynamic shapes, motion, and typography tokens from the M3 spec.
- Use `MaterialTheme` tokens (`colorScheme`, `typography`, `shapes`) exclusively; never hardcode colors or dimensions.
- Optimize layouts for all target platforms:
  - **Mobile (Android/iOS):** single-column, touch-friendly tap targets (≥48dp), bottom navigation.
  - **Desktop (JVM):** wider layouts with side navigation or rail, keyboard/mouse interactions, resizable windows.
- Use `WindowSizeClass` or equivalent to adapt layouts responsively across compact, medium, and expanded breakpoints.
- Prefer adaptive components (e.g. `NavigationSuiteScaffold`) over platform-specific nav patterns where possible.

### Dependencies
- All versions are managed in `gradle/libs.versions.toml`. Never hardcode versions.
- Do not add a new library without updating the version catalog.

### Platform
- Never use Android or iOS APIs directly in `commonMain`. Use `expect/actual`.
- `androidMain` and `iosMain` contain only thin platform bridges.

## What Agents Should NOT Do

- Do not modify `local.properties`.
- Do not push directly to `main` — open a PR.
- Do not add new Gradle modules without discussion.
- Do not introduce new networking libraries; use the existing HTTP client.
