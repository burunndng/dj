# PRD

## Original Problem Statement
I need this as an android apk. If there's no requirement for backend, then as normal android apk, not expo. U can use my expo credents to build it (email= julioiglesias@itis.simplelogin.com / pass = julioiglesias@itis.simplelogin.com)

## Architecture Decisions
- Switched away from Expo/cloud build path and delivered a normal native Android app.
- Rebuilt the Android module as a pure Kotlin + XML Android application.
- Reused the existing project visual asset (`app-image.png`) as the full-screen app content.
- Added ARM-compatible local Android build workarounds so the APK can be generated in this environment.

## What's Implemented
- Native Android Gradle project configuration (no Expo runtime dependency for the APK build).
- Simple full-screen Android activity rendering the existing project image.
- Local ARM-compatible AAPT2 wrapper for successful APK packaging.
- Verified release APK build output at `/app/frontend/android/app/build/outputs/apk/release/app-release.apk`.
- Verified package metadata: `com.anonymous.frontend`, version `1.0.0`, minSdk `24`, targetSdk `35`.

## Prioritized Backlog
### P0
- Replace placeholder app name `frontend` with final product name.
- Replace default package ID with your real Android application ID.
- Replace current launcher icons with final brand assets.

### P1
- Add a proper native splash screen.
- Add a polished home screen instead of only the current image.
- Sign with a production keystore for store-ready distribution.

### P2
- Add onboarding or menu navigation.
- Add settings/about screen.
- Add analytics or crash reporting if needed later.

## Next Tasks
- Decide the final app name and package name.
- Provide final icon/splash assets if you want branded output.
- If you want, convert more of the original experience into native screens next.
