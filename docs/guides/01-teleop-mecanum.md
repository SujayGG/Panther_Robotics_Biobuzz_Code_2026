# Guide 1 — TeleOp and the mecanum drivetrain

**Who this is for:** you've deployed code to the robot at least once (if not,
do the [README](../../README.md) first) and now want to actually drive.

**What you'll have at the end:** a field-centric mecanum TeleOp with a working
arm subsystem, and an understanding of why each line is there.

Work through this alongside the exercise files in
`TeamCode/src/main/java/org/firstinspires/ftc/teamcode/exercises/`. Read a few
sections, fill in that exercise, test it on the robot, come back.

> **Don't read this whole page first.** Read section 1–4, do Exercise 1, then
> continue. Robot code that you haven't run is code you don't understand yet.

---

## 1. The shape of an OpMode

Every OpMode is one of two styles, and the choice matters.

### LinearOpMode — code that runs top to bottom

```java
@TeleOp(name = "My TeleOp", group = "Competition")
public class MyTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        // setup: runs when the driver presses INIT
        waitForStart();              // blocks until PLAY
        while (opModeIsActive()) {
            // the match loop
        }
    }
}
```

This reads like a normal program. `waitForStart()` pauses, `opModeIsActive()`
becomes false when the driver presses STOP. You can use `sleep(500)` freely.

Use this for **autonomous** and for **learning**. Exercises 1–3 use it.

### OpMode — the iterative style

```java
@TeleOp(name = "My TeleOp", group = "Competition")
public class MyTeleOp extends OpMode {
    @Override public void init() { /* once, on INIT */ }
    @Override public void loop() { /* over and over, ~50+ times a second */ }
}
```

The SDK calls `loop()` repeatedly. **`loop()` must return quickly.** No
`Thread.sleep()`, no waiting for a motor to arrive. If `loop()` doesn't return,
the robot stops responding to the driver and your match is over.

Use this for **competition TeleOp**, because it forces the non-blocking
structure you need once you have subsystems. Exercise 4 uses it.

### The annotations

```java
@TeleOp(name = "Ex1: Tank Drive", group = "Exercises")
```

`name` is what appears on the Driver Station. `group` sorts the dropdown —
keep competition code in one group and experiments in another so nobody picks
`Test Arm Thing` during a match.

Swap `@TeleOp` for `@Autonomous` for the autonomous list. Without one of these
annotations, **your OpMode will not appear at all.**

And every sample from FIRST carries `@Disabled` — delete that line or your
OpMode stays hidden.

---

## 2. hardwareMap — asking for hardware by name

```java
DcMotor leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
```

Two things are happening: `DcMotor.class` says what kind of device you expect,
and `"left_drive"` is its name **in the robot's configuration file** on the
Driver Station.

That string must match **exactly**. Not `leftDrive`, not `Left_Drive`. Get it
wrong and the OpMode throws the instant the driver presses INIT, with an error
naming the device it couldn't find.

This is the most common failure in FTC, by a wide margin. Two habits fix it:

1. Get the real names from whoever configured the robot and write them
   somewhere the whole team can see.
2. When you add a new device, say so explicitly — "this needs a motor
   configured as `armMotor`" — so the config gets updated too.

Common types: `DcMotor`, `DcMotorEx` (adds velocity control), `Servo`,
`CRServo` (continuous rotation), `IMU`, `ColorSensor`, `DistanceSensor`.

---

## 3. Motors

```java
motor.setPower(0.5);    // -1.0 (full reverse) to 1.0 (full forward)
```

Power is a fraction of available voltage, not a speed. The same `0.5` is
faster on a fresh battery than a drained one, and slower going uphill. This is
why autonomous that relies on timing drifts — and why Guide 2 exists.

### Direction

Motors on opposite sides of the robot are mirror images, so the same power
spins them opposite ways and the robot turns instead of driving.

```java
leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
```

Don't reason about which side needs it — test. Push both sticks forward and
watch the wheels. Any wheel going the wrong way gets reversed.

### Zero-power behaviour

```java
motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // resists motion
motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);  // coasts
```

`BRAKE` for drivetrains — the robot stops when the driver lets go rather than
gliding. Essential for anything holding position against gravity.

### Run modes

| Mode | What it does |
|---|---|
| `RUN_WITHOUT_ENCODER` | Raw power. Default, fine for drivetrains. |
| `RUN_USING_ENCODER` | Motor holds a velocity. Smoother, slightly slower. |
| `RUN_TO_POSITION` | Motor drives itself to an encoder target. |
| `STOP_AND_RESET_ENCODER` | Sets the current position to zero. |

`STOP_AND_RESET_ENCODER` means "wherever this is right now is zero" — so the
mechanism must be in its home position when you call it, or every target is
off by a constant.

---

## 4. Gamepads

```java
double y = gamepad1.left_stick_y;    // NEGATIVE when pushed forward
double x = gamepad1.left_stick_x;    // positive right
boolean a = gamepad1.a;              // true while held
double t = gamepad1.left_trigger;    // 0.0 to 1.0
```

`gamepad1` and `gamepad2` are the two drivers.

### The Y-axis trap

**Pushing a stick forward gives a negative number.** It's a holdover from
screen coordinates, where down is positive. Almost every OpMode wants:

```java
double forward = -gamepad1.left_stick_y;
```

Forget the minus sign and your robot drives backwards. Everyone does this once.

### Rising-edge detection

`loop()` runs ~50 times a second; a human holds a button for maybe 200 ms. So
this fires ten times and lands on a coin flip:

```java
if (gamepad1.a) {
    clawOpen = !clawOpen;      // BROKEN — toggles ~10x per press
}
```

Compare against the previous value to fire exactly once per press:

```java
boolean currentA = gamepad1.a;
if (currentA && !previousA) {
    clawOpen = !clawOpen;      // exactly once, on the way down
}
previousA = currentA;          // remember for next loop
```

You will use this constantly. Learn it now.

> **→ Do Exercise 1 (`Exercise1_TankDrive.java`) before continuing.**

---

## 5. Why mecanum

Mecanum wheels have rollers set at 45°. Spin a wheel and it pushes both along
the robot *and* sideways. With four wheels you can cancel the forward
components and keep the sideways ones — so the robot strafes without turning.

Three independent things you can now command at once:

- **forward** — drive
- **strafe** — sideways
- **turn** — rotate

Being able to strafe while driving is what lets you line up on a scoring
element without a three-point turn.

---

## 6. The kinematics

Each wheel gets a signed sum of the three inputs:

```java
double frontLeftPower  = forward + strafe + turn;
double backLeftPower   = forward - strafe + turn;
double frontRightPower = forward - strafe - turn;
double backRightPower  = forward + strafe - turn;
```

Read the signs down each column rather than memorising the block:

- **forward** is `+` everywhere — all four wheels drive the same way.
- **turn** is `+` on the left, `−` on the right — left forward, right back.
- **strafe** alternates diagonally: `+` on front-left and back-right, `−` on
  the other two. That diagonal opposition is what makes the rollers walk the
  robot sideways.

Check one by hand. Set `strafe = 1`, everything else `0`: front-left and
back-right push one way, the other pair the other way. The forward components
cancel, the sideways ones add. Sideways motion.

**If strafing spins the robot instead**, a wheel's roller direction is wrong.
Looking down at the robot, the rollers should form an **X**.

---

## 7. Normalisation — the step people skip

Add three inputs and you can exceed 1.0. Full diagonal plus a turn gives
`1 + 1 + 1 = 3`. The motors clip at 1.0 — and clipping each wheel
independently **changes the direction the robot travels**, because it destroys
the ratios between wheels.

Divide everything by the largest magnitude instead:

```java
double max = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(turn), 1.0);

frontLeftPower  /= max;
backLeftPower   /= max;
frontRightPower /= max;
backRightPower  /= max;
```

`Math.max(..., 1.0)` is the clever bit: when the total is under 1.0 you divide
by 1.0 and nothing changes, so gentle inputs keep full resolution and only
oversized ones get scaled. The robot goes the direction the driver asked, just
capped in speed.

### Strafe correction

Mecanum wheels lose more to friction sideways than forward, so equal power
strafes less far than it drives:

```java
strafe *= 1.1;
```

Find your own number: strafe a measured distance, drive the same distance,
divide. Usually 1.0–1.2.

> **→ Do Exercise 2 (`Exercise2_MecanumDrive.java`) before continuing.**

---

## 8. Robot-centric vs field-centric

What you built in Exercise 2 is **robot-centric**: forward is wherever the
robot points. When the robot faces the driver, everything inverts and drivers
have to do the mental rotation mid-match. It costs seconds and causes crashes.

**Field-centric** means push the stick away from you and the robot drives away
from you, whatever direction it's facing. Drivers stop thinking about the
robot's orientation entirely. Once they try it they don't go back.

All it takes is knowing the robot's heading, and one rotation.

---

## 9. The IMU

The Control Hub has a built-in IMU that reports heading.

```java
IMU imu = hardwareMap.get(IMU.class, "imu");
imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
        RevHubOrientationOnRobot.LogoFacingDirection.UP,
        RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
imu.resetYaw();
```

Those two directions describe **how the hub is physically bolted to the
robot** — which way the REV logo faces, which way the USB ports face. Go look
at the robot; don't guess. Wrong values mean wrong headings, and field-centric
drive behaves bizarrely in a way that's hard to trace back here.

```java
double heading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
```

**Ask for radians.** `Math.sin` and `Math.cos` take radians. Asking for
degrees and feeding them to `cos()` gives drive that's subtly, maddeningly
wrong rather than obviously broken.

`imu.resetYaw()` makes the current direction the new zero. Bind it to a button
the driver can find without looking — they'll need it after a collision spins
the robot, and IMUs drift a little over a match.

---

## 10. The rotation

```java
double rotStrafe  = strafe * Math.cos(-heading) - forward * Math.sin(-heading);
double rotForward = strafe * Math.sin(-heading) + forward * Math.cos(-heading);
```

This is the standard 2D rotation matrix. The **minus** on heading is the whole
idea: the robot has rotated `+heading` relative to the field, so to express the
driver's field-frame request in the robot's frame you rotate back the other way.

Then feed `rotForward` and `rotStrafe` into the *same* kinematics from
section 6. The rotation is the only new code.

Check it by hand. Heading = 90° (robot turned left), driver pushes the stick
away (`forward = 1, strafe = 0`):

- `rotStrafe  = 0·cos(−90°) − 1·sin(−90°) = 1`
- `rotForward = 0·sin(−90°) + 1·cos(−90°) = 0`

Pure strafe. Which is right — the robot is sideways to the field, so driving
"away from the driver" means strafing from the robot's point of view.

> **→ Do Exercise 3 (`Exercise3_FieldCentric.java`) before continuing.**

---

## 11. Subsystems

Once you have a drivetrain, an arm, and an intake in one OpMode, that file
becomes 400 lines and two people can't work on it at once. A **subsystem** is
one mechanism in its own class, exposing intentions rather than motor powers.

```java
public class Arm {
    private final DcMotor motor;
    private int target = 0;

    public Arm(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "armMotor");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void raise() { target = 1000; }
    public void lower() { target = 0; }

    public void update() {
        int error = target - motor.getCurrentPosition();
        motor.setPower(Math.max(-0.8, Math.min(0.8, error * 0.005)));
    }
}
```

The OpMode then reads:

```java
if (gamepad1.dpad_up) arm.raise();
arm.update();
```

Three things this buys you: the OpMode stays readable, TeleOp and Autonomous
share the same arm logic, and two people can edit different mechanisms without
conflicts.

### update() and why it exists

`update()` does one small step of work and returns. Call it **every loop**.

This is how you obey the never-block rule. You can't wait for the arm to
arrive — so instead you nudge it a bit, 50 times a second, while the
drivetrain keeps responding.

**A subsystem whose `update()` isn't called does nothing, silently.** No error,
no telemetry, just a dead mechanism. Check this first when a new subsystem
won't move.

### Proportional control

```java
int error = target - current;          // how far off, and which way
double power = error * 0.005;          // far → fast, close → slow, there → stopped
power = Math.max(-0.8, Math.min(0.8, power));   // clamp
```

That's it — and it's enough for most FTC mechanisms. Start the gain small and
raise it until the arm arrives briskly without slamming. Too large and it
overshoots and oscillates.

This is the P in PID. Add the other terms only when P genuinely isn't enough —
usually it is.

> **→ Do Exercise 4 (`Exercise4_Subsystem.java` and
> `Exercise4_SubsystemTeleOp.java`).**

---

## 12. Habits that keep you fast

**Telemetry is your debugger.** There's no breakpoint on a moving robot.

```java
telemetry.addData("Arm target", target);
telemetry.addData("Arm current", motor.getCurrentPosition());
telemetry.update();      // nothing appears without this
```

**Watch your loop time.**

```java
ElapsedTime loopTimer = new ElapsedTime();
// at the top of loop(): loopTimer.reset();
// at the bottom:        telemetry.addData("Loop ms", loopTimer.milliseconds());
```

Under ~20 ms is healthy. If it climbs, something is too slow — usually a sensor
being read over I2C more often than needed.

**Read the real error.**

```bash
adb logcat -s RobotCore:V System.err:V
```

Full stack traces from the robot, live. Far better than guessing.

**Deadzone the sticks.** Controllers drift; a worn stick reads 0.02 at rest and
the robot creeps.

```java
if (Math.abs(input) < 0.05) input = 0;
```

**Add a slow mode.** Fine alignment at full speed is miserable.

```java
double speed = gamepad1.right_bumper ? 0.35 : 1.0;
```

---

## Where next

You now have a competition-capable TeleOp. The gap between this and an
advanced team is **autonomous** — and that means knowing where the robot is,
not just how long it's been driving.

That's [Guide 2 — Odometry and Pedro Pathing 3](02-odometry-pedro-pathing.md).
