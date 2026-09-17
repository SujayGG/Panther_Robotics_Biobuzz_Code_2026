package org.firstinspires.ftc.teamcode.exercises;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * EXERCISE 1 — Tank drive.
 *
 * <p>Goal: make the robot drive with two motors, left stick controlling the
 * left wheels and right stick controlling the right wheels.
 *
 * <p>This file compiles as-is, but the robot will not move until you fill in
 * the TODOs. That's deliberate — build it, deploy it, confirm it appears on the
 * Driver Station, and only then start filling things in. Getting an empty
 * OpMode onto the robot is the first skill.
 *
 * <p>Read guides/01-teleop-mecanum.md sections 1–4 before starting.
 */
@TeleOp(name = "Ex1: Tank Drive", group = "Exercises")
public class Exercise1_TankDrive extends LinearOpMode {

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    @Override
    public void runOpMode() {
        // ================================================================
        // TODO 1: Grab the two drive motors from the hardware map.
        //
        // The strings must match the robot configuration EXACTLY. Ask whoever
        // configured the robot what the drive motors are called, and replace
        // the names below if they differ.
        //
        // Example:
        //   leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        // ================================================================
        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // ================================================================
        // TODO 2: Reverse one side.
        //
        // The motors on opposite sides of the robot face opposite directions,
        // so identical power makes the robot spin instead of drive forward.
        // Reversing one side fixes it.
        //
        // Try it WITHOUT this first and watch what happens — understanding why
        // the robot spins is worth more than being told.
        //
        //   leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        // ================================================================

        // Brake when power is zero, instead of coasting. Try FLOAT later and
        // feel the difference when driving.
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Ready — press PLAY");
        telemetry.update();

        // Nothing runs until the driver presses PLAY.
        waitForStart();

        while (opModeIsActive()) {

            // ============================================================
            // TODO 3: Read the sticks.
            //
            // Pushing a stick FORWARD gives a NEGATIVE number. So to make
            // "forward stick" mean "forward robot", you need a minus sign.
            //
            //   double leftPower  = -gamepad1.left_stick_y;
            //   double rightPower = -gamepad1.right_stick_y;
            // ============================================================
            double leftPower = 0;
            double rightPower = 0;

            // ============================================================
            // TODO 4: Send the power to the motors.
            //
            //   leftDrive.setPower(leftPower);
            // ============================================================

            telemetry.addData("Left Power", "%.2f", leftPower);
            telemetry.addData("Right Power", "%.2f", rightPower);
            telemetry.update();
        }
    }
}
