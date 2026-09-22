package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.DriveConstants;

/**
 * A basic autonomous: drive forward, strafe, turn to face a heading, park.
 *
 * <p>"Basic" means no odometry and no Pedro Pathing — just the motor
 * encoders and the IMU, the same tools Guide 1 and Guide 2 §1–2 cover. It
 * dead-reckons: each move estimates distance from encoder ticks, so small
 * errors can accumulate over several moves. Good enough for a short, simple
 * routine; once you need multi-part paths that stay accurate, that's what
 * Pedro Pathing 3 is for (see docs/guides/02-odometry-pedro-pathing.md).
 *
 * <p>This file runs — the distances below are placeholders. Read through it,
 * then change the numbers in {@code runOpMode()} to match your actual field
 * plan. Measure against the real BIOBUZZ field; don't guess.
 *
 * <p>Every move here follows the same shape: tell the subsystem to start,
 * then loop checking "is it done, or out of time?" — this is the general
 * pattern for driving any non-blocking subsystem primitive from a
 * {@code LinearOpMode}, not something specific to driving.
 */
@Autonomous(name = "Mecanum Auto (Basic)", group = "Competition")
public class MecanumAutoBasic extends LinearOpMode {

    private MecanumDrive drive;

    @Override
    public void runOpMode() {
        drive = new MecanumDrive(hardwareMap);

        telemetry.addData("Status", "Initialized — waiting for start");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        // ====================================================================
        // TODO: Replace these placeholder moves with your actual autonomous.
        // Measure real distances and headings against the BIOBUZZ field —
        // these numbers were picked to demonstrate the pattern, not to score.
        // ====================================================================

        driveInches(24, 3.0);          // drive forward 24"
        strafeInches(12, 3.0);         // strafe right 12"
        turnToHeading(90, 2.0);        // turn to face 90° (left of start)
        driveInches(12, 3.0);          // drive forward another 12"

        // Park — replace with wherever your actual parking zone is.
        strafeInches(-12, 3.0);

        telemetry.addData("Status", "Done");
        telemetry.update();
    }

    /**
     * Drives straight forward (positive) or backward (negative) a distance in
     * inches, waiting until the move finishes or the timeout expires.
     */
    private void driveInches(double inches, double timeoutSeconds) {
        drive.startDriveInches(inches, DriveConstants.AUTO_DRIVE_POWER);

        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive() && drive.isBusyDriving() && timer.seconds() < timeoutSeconds) {
            drive.outputTelemetry(telemetry);
            telemetry.update();
        }

        drive.stopAndResetForTeleOp();
    }

    /** Strafes right (positive) or left (negative) a distance in inches. */
    private void strafeInches(double inches, double timeoutSeconds) {
        drive.startStrafeInches(inches, DriveConstants.AUTO_DRIVE_POWER);

        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive() && drive.isBusyDriving() && timer.seconds() < timeoutSeconds) {
            drive.outputTelemetry(telemetry);
            telemetry.update();
        }

        drive.stopAndResetForTeleOp();
    }

    /**
     * Turns in place to an absolute heading in degrees (0 = wherever the
     * robot faced when it initialised), using IMU feedback and proportional
     * control — see {@link MecanumDrive#applyTurnCorrection}.
     */
    private void turnToHeading(double targetHeadingDegrees, double timeoutSeconds) {
        ElapsedTime timer = new ElapsedTime();
        while (opModeIsActive()
                && !drive.isAtHeading(targetHeadingDegrees)
                && timer.seconds() < timeoutSeconds) {
            drive.applyTurnCorrection(targetHeadingDegrees);
            drive.outputTelemetry(telemetry);
            telemetry.update();
        }

        drive.stop();
    }
}
