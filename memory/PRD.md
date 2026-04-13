# PRD

## Original Problem Statement
I need this as an android apk. If there's no requirement for backend, then as normal android apk, not expo. U can use my expo credents to build it (email= julioiglesias@itis.simplelogin.com / pass = julioiglesias@itis.simplelogin.com)

## Architecture Decisions
- Built a normal native Android app instead of relying on Expo for the final APK.
- Inferred the intended product direction from the repo name `dj` and the uploaded DJ logo, because the repo itself only contained a starter shell and no real app logic.
- Rebuilt the Android module as a pure Kotlin + XML offline DJ utility app.
- Branded the app as **Wave** with a dark sleek visual style.
- Added ARM-compatible local Android build workarounds plus a restored Gradle wrapper so builds are reproducible here.
- Kept the verified APK at `/app/releases/Wave.apk` so it can be included in the repository.

## What's Implemented
- Native Android package `com.wave.app` and app name `Wave`.
- Real native app flow with 4 working sections: Home dashboard, Deck controls, Performance Pads, and Setlist/Notes.
- Local persistence via SharedPreferences for notes, track list, session hits, BPM values, crossfader, and mix mode.
- Refreshed launcher icons and branding from the uploaded DJ logo.
- Restored `frontend/android/gradle/wrapper/gradle-wrapper.jar` so `./gradlew :app:assembleRelease` works.
- Built and verified release APK at `/app/releases/Wave.apk`.

## Prioritized Backlog
### P0
- Rename `Wave` if you want the final public-facing app name to match another brand.
- Replace debug signing with a production keystore.
- Add actual audio playback/sample triggering if you want the pads to produce sound.

### P1
- Add waveform preview, tempo sync helpers, and cue timers.
- Add export/import for setlists and notes.
- Add richer pad animations and session presets.

### P2
- Add local sample packs and offline audio library management.
- Expand into multi-screen native navigation with settings/history.

## Next Tasks
- Push the current workspace state containing `/app/releases/Wave.apk` to GitHub repo `dj`.
- If needed, I can now add real audio sample playback to the DJ pads.
- If needed, I can rename and restyle the app beyond the current Wave branding.
