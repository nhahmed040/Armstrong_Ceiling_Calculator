# Armstrong Ceiling Calculator — Version 5.5

Android WebView application for Armstrong 600 × 600 mm ceiling layout and material estimating.

## Version 5.5 updates — Layout/takeoff consistency and edge-bay correction
- Adds room-by-room cutting plans for 3.6 m main-runner stock and 3.0 m wall-angle stock (editable in Settings).
- Reuses suitable offcuts within the same room before transferring remaining reusable offcuts to later rooms.
- Separates same-room reuse, cross-room reuse, reusable offcuts and scrap; offcuts shorter than 300 mm are treated as scrap.
- Applies the configured saw kerf when a cut leaves a remainder.
- Uses optimized new-stock bar counts for the plan requirement and additional-material comparison.
- Shows room cut segments and stock-bar details in the Materials tab; includes optimization totals in PDF and CSV exports.
- Uses one shared room-grid geometry for the plan drawing and material takeoff, so main-runner and cross-tee counts follow the visible layout.
- Keeps 600 mm modules at the perimeter: when an edge bay exceeds 600 mm, place the 600 mm tee from the nearest main runner and leave the remainder at the wall; omit the tee when the edge bay is 600 mm or narrower.
- Reports scrap generated within each room as well as the project-wide total.
- Preserves the Version 5 settings menu, 600 × 600 mm board size, two grid alignment modes, live plan and package-costing basis.

## Costing basis
The 2,100 ETB/m² rate remains the complete ceiling package (boards plus standard market-ratio grid allowances). Only grid quantities above the included allowance are priced separately. Market allowances continue to round to the nearest whole unit.

## Build APK
The repository workflow builds the updated `ArmstrongCeilingCalculator` source directory directly; it does not extract or build the archived V5.4 ZIP. Push to `main`/`master` or run the workflow manually, then download the `Armstrong-Ceiling-Calculator-v5.5-APK` artifact.

The embedded JavaScript syntax was checked after the V5.5 changes. The Android APK must still complete the GitHub Actions build and then be installed and tested on a phone.


## Version 5.5 grid rule update
- 1200 mm cross-tee stations are drawn across each main-runner bay.
- 600 mm green tees complete internal bays; edge bays wider than 600 mm retain a 600 mm module with any remainder left at the perimeter.
- The plan and takeoff share the same generated tee positions and bay segmentation.
- Both Balanced Centered and Corner Bound layouts use the same connection rules, with their own edge measurements.
