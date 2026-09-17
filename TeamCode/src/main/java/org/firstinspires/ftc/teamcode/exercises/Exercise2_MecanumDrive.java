package org.firstinspires.ftc.teamcode.exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * EXERCISE 2 — Mecanum drive (robot-centric).
 *
 * <p>Goal: four motors, and the robot can now strafe sideways as well as drive
 * and turn. "Robot-centric" means forward is whichever way the robot is facing.
 *
 * <p>The interesting part is the kinematics: each of the four wheels gets a
 * different combination of forward, strafe, and turn. Work out on paper which
 * way each wheel must spin to strafe right before you write the formulas —
 * copying them without understanding will cost you an afternoon later.
 *
 * <p>Read guides/01-teleop-mecanum.md sections 5–7 before starting.
 */
@TeleOp(name = "Ex2: Mecanum Drive", group = "Exercises")
public class Exercise2_MecanumDrive extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {
        // TODO 1: Fetch all four motors. Names must match the robot config.
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // ================================================================
        // TODO 2: Reverse the left side.
        //
        //   frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //   backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //
        // Verify before trusting it: push both sticks forward and check all
        // four wheels spin the same way. If one is backwards, reverse that one.
        // ================================================================

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Ready — press PLAY");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // TODO 3: Read the three axes of driver input.
            //   forward: -gamepad1.left_stick_y   (negate!)
            //   strafe:   gamepad1.left_stick_x
            //   turn:     gamepad1.right_stick_x
            double forward = 0;
            double strafe = 0;
            double turn = 0;

            // ============================================================
            // TODO 4: Mecanum kinematics.
            //
            // Each wheel is the sum of the three inputs, with signs that
            // depend on where the wheel sits and which way its rollers point:
            //
            //   frontLeft  = forward + strafe + turn
            //   backLeft   = forward - strafe + turn
            //   frontRight = forward - strafe - turn
            //   backRight  = forward + strafe - turn
            //
            // Sanity-check one: to strafe RIGHT (strafe = +1, others 0) the
            // front-left and back-right push one way while front-right and
            // back-left push the other. That opposition is what makes the
            // rollers walk the robot sideways.
            // ============================================================
            double frontLeftPower = 0;
            double backLeftPower = 0;
            double frontRightPower = 0;
            double backRightPower = 0;

            // ============================================================
            // TODO 5: Normalise.
            //
            // Add three inputs together and you can exceed 1.0, which the
            // motors clip — and clipping each wheel separately CHANGES THE
            // DIRECTION the robot travels. Divide them all by the largest
            // magnitude instead, so the ratios between wheels survive:
            //
            //   double max = Math.max(Math.abs(forward) + Math.abs(strafe)
            //                         + Math.abs(turn), 1.0);
            //   frontLeftPower /= max;   // and the other three
            //
            // Using max(..., 1.0) means gentle inputs are left alone and only
            // oversized ones get scaled down.
            // ============================================================

            // TODO 6: Push the four values to the four motors.

            telemetry.addData("FL / FR", "%.2f / %.2f", frontLeftPower, frontRightPower);
            telemetry.addData("BL / BR", "%.2f / %.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
