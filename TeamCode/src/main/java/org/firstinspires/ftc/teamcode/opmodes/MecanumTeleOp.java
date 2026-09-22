package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;

/**
 * Competition TeleOp: field-centric mecanum drive.
 *
 * <p>Left stick moves the robot relative to the FIELD, not to whichever way
 * it happens to be facing — push it away from you and the robot drives away
 * from you. Right stick turns. Hold the right bumper for slow mode, tap back
 * to re-zero the heading if the robot gets knocked around.
 *
 * <p>Wheel names, directions, hub orientation, and speeds all come from
 * {@link org.firstinspires.ftc.teamcode.util.DriveConstants} — fix those
 * there before wondering why this file behaves wrong on your robot.
 */
@TeleOp(name = "Mecanum TeleOp", group = "Competition")
public class MecanumTeleOp extends OpMode {

    private MecanumDrive drive;
    private ElapsedTime loopTimer;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        loopTimer = new ElapsedTime();

        telemetry.addData("Status", "Initialized — press Back any time to reset heading");
        telemetry.update();
    }

    @Override
    public void loop() {
        loopTimer.reset();

        if (gamepad1.back) {
            drive.resetHeading();
        }

        // Pushing a stick forward gives a NEGATIVE y — negate it.
        double forward = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        // Small deadzone — worn sticks drift and otherwise creep the robot.
        if (Math.abs(forward) < 0.05) forward = 0;
        if (Math.abs(strafe) < 0.05) strafe = 0;
        if (Math.abs(turn) < 0.05) turn = 0;

        boolean slowMode = gamepad1.right_bumper;

        drive.driveFieldCentric(forward, strafe, turn, slowMode);

        drive.outputTelemetry(telemetry);
        telemetry.addData("Slow mode", slowMode ? "ON" : "off");
        telemetry.addData("Loop time (ms)", "%.1f", loopTimer.milliseconds());
        telemetry.update();
    }
}
