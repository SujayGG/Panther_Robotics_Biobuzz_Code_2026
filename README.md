# PESH Robotics — BioBuzz 2026

FTC robot code for the **DECODE (2025–2026)** season.

Built on the official [FTC SDK](https://github.com/FIRST-Tech-Challenge/FtcRobotController)
**v11.2.1**, with an MCP server wired in so Claude Code (and Cursor, VS Code
Copilot, Windsurf, …) can write FTC code against current, verified APIs.

## Repository layout

```
FtcRobotController/          Stock FTC SDK app module — do not edit
TeamCode/                    Our code
  src/main/java/org/firstinspires/ftc/teamcode/
    opmodes/                 @TeleOp and @Autonomous OpModes
    subsystems/              Drivetrain, intake, lift, … hardware wrappers
    util/                    Constants, tuning configs, helpers
build.dependencies.gradle    Add third-party libraries here
.mcp.json                    ftc-mcp server, shared by the whole team
CLAUDE.md                    Project rules Claude Code reads automatically
docs/                        Setup guides
```

## Getting started

1. **[docs/ANDROID_STUDIO_SETUP.md](docs/ANDROID_STUDIO_SETUP.md)** — install
   Android Studio, open the project, build, and deploy to the robot.
2. **[docs/MCP_SETUP.md](docs/MCP_SETUP.md)** — get the `ftc` MCP server running
   in Claude Code and in whatever editor you use.

Short version, once Android Studio and Node.js are installed:

```bash
git clone https://github.com/SujayGG/Panther_Robotics_Biobuzz_Code_2026.git
cd Panther_Robotics_Biobuzz_Code_2026
./gradlew assembleDebug     # first run downloads Gradle + the SDK, takes a few minutes
```

Then open the folder in Android Studio and let it sync.

## Everyday commands

| Command | What it does |
|---|---|
| `./gradlew assembleDebug` | Compile everything — the quickest way to catch errors |
| `./gradlew installDebug` | Build and push the app to a connected Robot Controller |
| `adb devices` | Check the Control Hub / RC phone is connected |
| `adb connect 192.168.43.1:5555` | Connect to a Control Hub over Wi-Fi |

## Working with Claude Code

Run `claude` in this directory. It picks up `CLAUDE.md` and the `ftc` MCP server
automatically. Useful openers:

- "Scan the project and tell me what's set up."
- "Create a field-centric mecanum TeleOp with slow mode on the right bumper."
- "Add a lift subsystem with dashboard-tunable positions."
- "Validate the OpMode I just wrote."

The FTC-specific behaviour Claude follows in this repo — which packages code
goes in, when to use the MCP, and the SDK gotchas — is written down in
[CLAUDE.md](CLAUDE.md). Edit that file as the team's conventions change.

## SDK reference

The upstream FTC SDK README, with the season's release notes and links to the
official tutorials, is kept at [docs/FTC_SDK_README.md](docs/FTC_SDK_README.md).
