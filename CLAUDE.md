# PESH Robotics — BioBuzz 2026

FTC robot code for the DECODE (2025–2026) season. This repo is the FTC SDK
project (`FtcRobotController` v11.2.1) plus our team module, `TeamCode`.

## Where our code goes

All team code lives in `TeamCode/src/main/java/org/firstinspires/ftc/teamcode/`:

| Package      | Contents                                                        |
|--------------|-----------------------------------------------------------------|
| `opmodes/`   | `@TeleOp` / `@Autonomous` OpModes — driver-facing entry points   |
| `subsystems/`| Hardware wrappers (drivetrain, intake, lift, …) — no OpMode logic|
| `util/`      | Constants, tuning configs, math/helper classes                   |

**Never edit `FtcRobotController/`, `build.common.gradle`, or `libs/`** — those
are the stock SDK, and editing them makes future SDK upgrades painful. Module
customisations belong in `TeamCode/build.gradle`; new library dependencies go in
`build.dependencies.gradle`.

## Use the `ftc` MCP server

This project ships an MCP server (`ftc-mcp`) that carries verified, current FTC
API docs — Pedro Pathing 2.1, FTC Dashboard, Panels, Road Runner, FTCLib, and
the full hardware API. Model training data for these libraries is outdated, so
**prefer the MCP over recalled API knowledge**:

- Call `scan_project` with this repo's path at the start of a session — it
  reports the SDK version, which libraries are actually installed, and the
  existing OpModes, hardware device names, and `@Config` classes.
- Call `search_knowledge` before writing against any library or hardware API
  (motors, servos, IMU, Pinpoint, OTOS, VisionPortal, Limelight, Pedro paths,
  dashboard config).
- Call `validate_ftc_code` on new or edited OpModes before saying they're done.
- The server also exposes `ftc://…` resources and workflow prompts
  (`create-autonomous`, `create-subsystem`, `tune-pid`, `setup-gradle`, …).

If a `search_knowledge` result contradicts what you remember about an API,
the MCP is right.

## Build and deploy

```bash
./gradlew assembleDebug                 # compile — the fast correctness check
./gradlew :TeamCode:assembleDebug       # compile TeamCode only
./gradlew installDebug                  # build + push to a connected Robot Controller
adb devices                             # confirm the RC phone / Control Hub is attached
```

Deploying to a Control Hub over Wi-Fi: connect to the hub's network, then
`adb connect 192.168.43.1:5555`.

There is no unit-test suite — a clean `assembleDebug` plus a check on the robot
is our verification. Always compile before reporting an OpMode as finished.

## FTC conventions that bite

- Hardware names in `hardwareMap.get(...)` must match the Robot Controller's
  configuration file exactly. If you introduce a new device, say what the
  config entry needs to be named.
- Iterative `OpMode` (`init`/`loop`) must never block — no `Thread.sleep`, no
  `while` loops waiting on hardware. Use a state machine. `LinearOpMode` may
  block, via `sleep()` and `opModeIsActive()`.
- Gamepad Y axes are inverted (pushing forward gives negative). Negate them.
- `@Config` fields for live dashboard tuning must be `public static` and must
  **not** be `final`; read them fresh each loop rather than copying into a
  local at init, or tuning changes won't take effect.
- Prefer bulk reads (`LynxModule` `BULK_CACHE_MODE.MANUAL`, cleared once per
  loop) when loop times matter.
- Every OpMode needs `@TeleOp` or `@Autonomous` to appear on the Driver Station.

## Adding a library

Add dependencies to `build.dependencies.gradle`, never to `build.common.gradle`.
Ask the `ftc` MCP (`setup-gradle` prompt, or `ftc://gradle/all-library-coords`)
for the exact Maven coordinates and repositories. Note that Pedro Pathing
requires `compileSdkVersion 34`, which is set in `build.common.gradle` — that is
the one sanctioned edit to that file.

## Git

Work on a branch, commit with a clear message, and open a PR against `main`.
Don't commit `local.properties`, build outputs, or `.idea/`.
