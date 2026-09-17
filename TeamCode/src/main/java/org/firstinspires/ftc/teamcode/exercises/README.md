# Exercises

Fill-in-the-blank OpModes that go with
[`docs/guides/01-teleop-mecanum.md`](../../../../../../../../../docs/guides/01-teleop-mecanum.md).

## How to use them

1. **Build and deploy before changing anything.** They compile as-is. Confirm
   they show up on the Driver Station under the **Exercises** group. Getting an
   empty OpMode onto the robot is the first skill, and it's worth separating
   from "did my code work".
2. Read the guide sections named at the top of the file.
3. Fill in the TODOs in order. Each explains *why*, not just what to type.
4. Deploy and test on the robot after each one.

Filled-in exercises are yours — edit them freely, they're for learning. Don't
build competition code on top of them; that goes in `opmodes/` and
`subsystems/`.

## Order

| File | What you build |
|---|---|
| `Exercise1_TankDrive.java` | Two motors, tank drive |
| `Exercise2_MecanumDrive.java` | Four motors, strafing |
| `Exercise3_FieldCentric.java` | IMU, field-centric drive |
| `Exercise4_Subsystem.java` | An arm subsystem — not an OpMode, won't appear on the DS |
| `Exercise4_SubsystemTeleOp.java` | The OpMode that drives the arm |

## If nothing appears on the Driver Station

1. Did the install actually succeed? Scroll back for errors.
2. Is the class annotated `@TeleOp` or `@Autonomous`?
3. Did the Robot Controller restart? Restart it manually.
4. `Exercise4_Subsystem` is *supposed* to be absent — it's a plain class.

## Hardware names

Every exercise looks up motors by name, and those strings must match the robot
configuration **exactly**. The defaults here (`left_drive`, `frontLeft`,
`armMotor`, …) are guesses. Get the real names from whoever configured the
robot, and fix them in the file before wondering why it crashes on INIT.
