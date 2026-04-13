# PRD

## Original Problem Statement
I need this as an android apk. If there's no requirement for backend, then as normal android apk, not expo. U can use my expo credents to build it (email= julioiglesias@itis.simplelogin.com / pass = julioiglesias@itis.simplelogin.com)

## Architecture Decisions
- Delivered a normal native Android app instead of relying on Expo for the final APK.
- Rebuilt the Android module as a pure Kotlin + XML application.
- Branded the app as **Wave** with a dark sleek visual style.
- Used the user-provided DJ logo as the in-app mark and launcher icon source.
- Added an ARM-compatible local Android build workaround so APK generation succeeds in this environment.
- Copied the verified APK to `/app/releases/Wave.apk` so it can be included in the repository.

## What's Implemented
- Native Android package renamed to `com.wave.app`.
- App name updated to `Wave`.
- Branded dark home screen with logo, title, tagline, and cleaner layout.
- Generated launcher icons and splash/logo assets from the uploaded image.
- Built and verified working release APK at `/app/releases/Wave.apk`.
- Verified APK metadata: package `com.wave.app`, app label `Wave`, minSdk `24`, targetSdk `35`.

## Prioritized Backlog
### P0
- Replace temporary debug signing with production keystore signing.
- Confirm final Play Store-ready package ID if different from `com.wave.app`.
- Add real app functionality beyond the branded launch screen.

### P1
- Add a proper native splash transition and onboarding flow.
- Add music/DJ-specific home features, navigation, and content screens.
- Replace temporary tagline/caption with final product copy.

### P2
- Add settings, sharing, analytics, and crash reporting.
- Add theme toggles or richer motion polish.

## Next Tasks
- Push the repository state containing `/app/releases/Wave.apk` to GitHub.
- Decide whether to keep `com.wave.app` or replace it with a final production package name.
- Expand the branded Android shell into the full Wave app experience.
