package org.firstinspires.ftc.teamcode.exercises;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * EXERCISE 4a — Your first subsystem.
 *
 * <p>A subsystem is one mechanism wrapped in a class. It owns its hardware and
 * exposes intentions — {@code raise()}, {@code lower()}, {@code openClaw()} —
 * rather than motor powers.
 *
 * <p>Why bother? Three reasons that show up fast:
 *
 * <ul>
 *   <li>Your OpMode stays readable. {@code arm.raise()} beats twelve lines of
 *       encoder arithmetic inline.</li>
 *   <li>TeleOp and Autonomous share it. Write the arm logic once; both use it.</li>
 *   <li>Two people can work at once without fighting over the same file.</li>
 * </ul>
 *
 * <p>This class is NOT an OpMode — it has no {@code @TeleOp} annotation and
 * won't appear on the Driver Station. {@link Exercise4_SubsystemTeleOp} drives it.
 *
 * <p>Read guides/01-teleop-mecanum.md section 11 before starting.
 */
public class Exercise4_Subsystem {

    /**
     * Encoder position for the arm's "up" position. You'll find the real
     * number experimentally in Exercise 4b — move the arm by hand, read the
     * encoder off telemetry, write the number down.
     */
    public static final int ARM_UP = 1000;

    /** Encoder position for the arm's "down" position. */
    public static final int ARM_DOWN = 0;

    /** Servo positions. Servos take 0.0 to 1.0, never encoder ticks. */
    public static final double CLAW_OPEN = 0.6;
    public static final double CLAW_CLOSED = 0.15;

    private final DcMotor armMotor;
    private final Servo clawServo;

    private int targetPosition = ARM_DOWN;

    /**
     * A subsystem takes the hardwareMap in its constructor and looks up its
     * own hardware. The OpMode passes its own {@code hardwareMap} in.
     */
    public Exercise4_Subsystem(HardwareMap hardwareMap) {
        // TODO 1: Fetch the arm motor and claw servo. Names must match the
        // robot configuration.
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        clawServo = hardwareMap.get(Servo.class, "clawServo");

        // ================================================================
        // TODO 2: Zero the encoder, then pick a run mode.
        //
        // STOP_AND_RESET_ENCODER says "however the arm is sitting right now,
        // call that position zero". So the arm must be physically DOWN when
        // the OpMode initialises, or every target will be off by a constant.
        //
        //   armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //   armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //
        // BRAKE makes the arm resist gravity at zero power instead of dropping.
        //   armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // ================================================================
    }

    /** Sends the arm toward its up position. */
    public void raise() {
        // TODO 3: Set targetPosition to ARM_UP.
    }

    /** Sends the arm toward its down position. */
    public void lower() {
        // TODO 4: Set targetPosition to ARM_DOWN.
    }

    public void openClaw() {
        // TODO 5: clawServo.setPosition(CLAW_OPEN);
    }

    public void closeClaw() {
        // TODO 6: clawServo.setPosition(CLAW_CLOSED);
    }

    /**
     * Drives the motor toward the target. MUST be called every loop — a
     * subsystem that isn't updated does nothing, and this is the single most
     * common reason a new subsystem "doesn't work".
     */
    public void update() {
        // ================================================================
        // TODO 7: The simplest useful controller — proportional control.
        //
        // Power proportional to how far away you are: far means fast, close
        // means slow, there means stopped. No oscillation, no overshoot, and
        // it's about ten lines less than a full PID.
        //
        //   int error = targetPosition - armMotor.getCurrentPosition();
        //   double power = error * 0.005;               // tune this gain
        //   power = Math.max(-0.8, Math.min(0.8, power)); // clamp it
        //   armMotor.setPower(power);
        //
        // Start the gain small. Too large and the arm slams into its limits.
        // ================================================================
    }

    /** @return true when the arm is close enough to its target to act on. */
    public boolean isAtTarget() {
        // TODO 8: Return whether |target - current| is under ~20 ticks.
        // Never test for exact equality — the arm will never land on a single
        // tick and your code will wait forever.
        return false;
    }

    public int getCurrentPosition() {
        return armMotor.getCurrentPosition();
    }

    /** Push the subsystem's state to the Driver Station for debugging. */
    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("Arm target", targetPosition);
        telemetry.addData("Arm current", armMotor.getCurrentPosition());
        telemetry.addData("Arm at target", isAtTarget());
        telemetry.addData("Claw", "%.2f", clawServo.getPosition());
    }
}
