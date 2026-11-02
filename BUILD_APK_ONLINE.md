# Build the installable APK online (no Android Studio required on your phone)

This project includes a GitHub Actions workflow that compiles the Android app on a cloud runner.

1. On a computer, sign in to GitHub and create a new repository (for example `CeilingFloorCalculator`).
2. Upload the **contents** of this project folder to the repository's `main` branch. Keep `.github/workflows/android.yml` in its exact path.
3. Open the repository's **Actions** tab and select **Build Android APK**.
4. Choose **Run workflow** (or let the workflow run after pushing to `main`) and wait for the job to finish successfully.
5. Open the completed workflow run. Under **Artifacts**, download `CeilingFloorCalculator-debug-apk` and unzip it to get `app-debug.apk`.
6. Transfer `app-debug.apk` to your Android phone and install it. If Android asks, allow installation from that file manager/browser. Only install APKs you built from source you trust.

The output is a debug-signed APK for testing/personal use, not a Play Store release. A release APK should be signed with your own private release key.
