# Armstrong Ceiling Calculator — Version 5.3

Android WebView application for Armstrong 600 × 600 mm ceiling layout and material estimating.

## Version 5.3 updates — Grid edge-bay correction and smart material optimization
- Adds room-by-room cutting plans for 3.6 m main-runner stock and 3.0 m wall-angle stock (editable in Settings).
- Reuses suitable offcuts within the same room before transferring remaining reusable offcuts to later rooms.
- Separates same-room reuse, cross-room reuse, reusable offcuts and scrap; offcuts shorter than 300 mm are treated as scrap.
- Applies the configured saw kerf when a cut leaves a remainder.
- Uses optimized new-stock bar counts for the plan requirement and additional-material comparison.
- Shows room cut segments and stock-bar details in the Materials tab; includes optimization totals in PDF and CSV exports.
- Corrects the grid edge-bay rule: green 600 mm cross tees appear only in internal 1,200 mm bays, never in either perimeter bay, and the Materials count follows the same rule.
- Reports scrap generated within each room as well as the project-wide total.
- Preserves the Version 5 settings menu, 600 × 600 mm board size, two grid alignment modes, live plan and package-costing basis.

## Costing basis
The 2,100 ETB/m² rate remains the complete ceiling package (boards plus standard market-ratio grid allowances). Only grid quantities above the included allowance are priced separately. Market allowances continue to round to the nearest whole unit.

## Build APK
Upload the contents of the `ArmstrongCeilingCalculator` folder to the GitHub repository root, then run **Build Android APK** under Actions (or push to `main`/`master`). Download the `Armstrong-Ceiling-Calculator-APK` artifact after the workflow succeeds.

The JavaScript syntax and ZIP integrity are checked before delivery. A remote Android build and physical-device test are not performed here; run the GitHub Actions workflow to build the APK, then test this new version on your phone.


## Version 5.3 grid rule update
- 1200 mm cross-tee stations are drawn across each main-runner bay.
- 600 mm green tees complete internal bays and are also added in either perimeter bay when its measured edge opening is 600 mm or less.
- The 600 mm tee quantity is calculated from the same bay eligibility rule used by the drawing.
- Both Balanced Centered and Corner Bound layouts use the same connection rules, with their own edge measurements.
