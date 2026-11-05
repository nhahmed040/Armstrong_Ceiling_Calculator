# Build Armstrong Ceiling Calculator APK with GitHub Actions

1. Upload the contents of the `ArmstrongCeilingCalculator` folder to the root of your GitHub repository.
2. Open the repository's **Actions** tab.
3. Select **Build Android APK**.
4. Tap **Run workflow** and confirm.
5. When the run succeeds, open it and download the **Armstrong-Ceiling-Calculator-APK** artifact.
6. Extract the ZIP and install `Armstrong_Ceiling_Calculator.apk` on your Android phone.

The app keeps the existing Android application ID so a newer build can update the already-installed app rather than creating a second app, provided the signing key matches.
