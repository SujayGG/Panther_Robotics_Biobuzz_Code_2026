package org.firstinspires.ftc.teamcode.exercises;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * EXERCISE 4b — Driving a subsystem from an OpMode.
 *
 * <p>Note this extends {@link OpMode}, not {@code LinearOpMode} like the
 * earlier exercises. This is the ITERATIVE style: the SDK calls {@code init()}
 * once and then {@code loop()} over and over, roughly 50+ times a second.
 *
 * <p>The rule that comes with it: <b>{@code loop()} must never block.</b> No
 * {@code Thread.sleep()}, no {@code while} loop waiting for the arm to arrive.
 * Return quickly, every time, and let the next call continue the work. Block
 * here and the robot stops responding to the driver — match over.
 *
 * <p>That constraint is exactly why {@code update()} exists on the subsystem:
 * instead of waiting for the arm, you nudge it a little on every pass.
 *
 * <p>Read guides/01-teleop-mecanum.md sections 11–12 before starting.
 */
@TeleOp(name = "Ex4: Subsystem TeleOp", group = "Exercises")
public class Exercise4_SubsystemTeleOp extends OpMode {

    private Exercise4_Subsystem arm;
    private ElapsedTime loopTimer;

    // Used to detect the MOMENT a button goes down, rather than the whole
    // time it's held. See TODO 3.
    private boolean previousA = false;

    @Override
    public void init() {
        // TODO 1: Build the subsystem, handing it this OpMode's hardwareMap.
        //   arm = new Exercise4_Subsystem(hardwareMap);

        loopTimer = new ElapsedTime();

        telemetry.addData("Status", "Initialised — make sure the arm is DOWN");
        telemetry.update();
    }

    @Override
    public void loop() {
        loopTimer.reset();

        // ============================================================
        // TODO 2: Hold-to-act bindings.
        //
        // Reads the button every loop, so the action repeats while held. Right
        // for things with no "state" — a continuous intake, say.
        //
        //   if (gamepad1.dpad_up) {
        //       arm.raise();
        //   } else if (gamepad1.dpad_down) {
        //       arm.lower();
        //   }
        // ============================================================

        // ============================================================
        // TODO 3: A toggle, using rising-edge detection.
        //
        // Why the extra variable? loop() runs ~50x a second, and a human holds
        // a button for maybe 200ms — so a naive `if (gamepad1.a) toggle();`
        // fires ten times and lands on a coin flip. Comparing against the
        // previous value fires exactly once per press:
        //
        //   boolean currentA = gamepad1.a;
        //   if (currentA && !previousA) {
        //       // the frame the button went DOWN
        //       clawOpen = !clawOpen;
        //   }
        //   previousA = currentA;
        //
        // This pattern shows up constantly. Learn it once.
        // ============================================================

        // ============================================================
        // TODO 4: Update the subsystem — every single loop, no exceptions.
        //
        //   arm.update();
        //
        // Forget this and the arm simply won't move, with no error to tell
        // you why. Check it first when a subsystem seems dead.
        // ============================================================

        // TODO 5: arm.outputTelemetry(telemetry);

        // Loop time is your performance canary. Healthy is under ~20 ms. If it
        // climbs, something in the loop is too slow — usually reading a sensor
        // over I2C more often than needed.
        telemetry.addData("Loop time (ms)", "%.1f", loopTimer.milliseconds());
        telemetry.update();
    }
}
