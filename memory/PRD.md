# PRD

## Original Problem Statement
I need this as an android apk. If there's no requirement for backend, then as normal android apk, not expo. U can use my expo credents to build it (email= julioiglesias@itis.simplelogin.com / pass = julioiglesias@itis.simplelogin.com)

## Architecture Decisions
- Delivered a normal native Android app instead of relying on Expo for the final APK.
- Rebuilt the Android module as a pure Kotlin + XML application.
- Branded the app as **Wave** with a dark sleek visual style.
- Used the user-provided DJ logo as the in-app mark and launcher icon source.
- Implemented a short branded splash, then loads the user-provided target in a native Android WebView.
- Target URL currently configured: `https://github.com/burunndng/dj/tree/main`
- Kept the verified APK at `/app/releases/Wave.apk` so it can be included in the repository.

## What's Implemented
- Native Android package renamed to `com.wave.app`.
- App name updated to `Wave`.
- Branded splash screen and WebView-based app flow.
- Internet permission added for WebView loading.
- Generated launcher icons and splash/logo assets from the uploaded image.
- Built and verified working release APK at `/app/releases/Wave.apk`.
- Verified APK metadata: package `com.wave.app`, app label `Wave`, minSdk `24`, targetSdk `35`.

## Prioritized Backlog
### P0
- Replace the GitHub URL with the final live app/site URL if the APK should open the real product instead of the repo page.
- Replace temporary debug signing with production keystore signing.
- Confirm final Play Store-ready package ID if different from `com.wave.app`.

### P1
- Add loading/error states for offline or failed WebView loads.
- Add navigation controls (refresh/back/home) inside the app shell.
- Replace temporary tagline/caption with final product copy.

### P2
- Add richer splash animation and theme polish.
- Expand from WebView shell into native screens if needed later.

## Next Tasks
- Push the current workspace state containing `/app/releases/Wave.apk` to GitHub.
- If desired, swap the WebView target from the GitHub repo page to the final live app URL.
- Decide whether to keep `com.wave.app` or replace it with the production package name.
