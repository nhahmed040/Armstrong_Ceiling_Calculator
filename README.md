# Armstrong Ceiling Calculator — Version 5

Android WebView application for Armstrong 600 × 600 mm ceiling layout and material estimating.

## Version 5 updates
- Moved **Ceiling board & grid settings** out of Room Setup into a compact three-dot menu and scrollable settings dialog.
- Simplified Room Setup to room dimensions and room controls only.
- App display name, project branding, workflow artifact and APK project version updated to Armstrong Ceiling Calculator.
- Standard market-ratio allowances now use nearest-whole-unit rounding (`Math.round`), not always-up rounding.
- The 2,100 ETB/m² base rate is treated as the complete ceiling package (boards plus standard market-ratio grid quantities); only grid quantities above the included allowance are priced individually.
- Existing room layout, alignment, live plan, material breakdown, CSV and PDF functions are retained.

## Build APK
Open the repository in GitHub and run the **Build Android APK** workflow under Actions (or push to `main`/`master`). Download the `Armstrong-Ceiling-Calculator-APK` artifact after the workflow succeeds.

The ZIP and JavaScript syntax will be checked before delivery. Android APK build and on-device testing have not yet been performed for this version.
