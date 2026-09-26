package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.util.DriveConstants;

/**
 * Four-motor mecanum drivetrain: robot-centric and field-centric driving for
 * TeleOp, plus encoder- and IMU-based primitives for a plain-SDK autonomous.
 *
 * <p>Every number specific to a physical robot — wheel size, motor encoder
 * resolution, which side is reversed, how the hub is mounted — lives in
 * {@link DriveConstants}, not here. Swap chassis, edit that file, this class
 * doesn't change.
 *
 * <p><b>This class deliberately never checks {@code opModeIsActive()} or runs
 * a loop of its own.</b> Per this repo's convention (see CLAUDE.md),
 * subsystems are hardware wrappers, not OpMode logic — so the autonomous
 * primitives below (see section 2) do one step of work and return; the OpMode
 * using them owns the "keep going until done or timed out" loop. See
 * {@code opmodes/MecanumAutoBasic.java} for what that loop looks like.
 *
 * <p>Robot configuration must define four {@code DcMotorEx} entries and an
 * {@code IMU}, named per {@link DriveConstants}.
 */
public class MecanumDrive {

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private final IMU imu;

    private double lastHeadingRadians = 0.0;

    public MecanumDrive(HardwareMap hardwareMap) {
        frontLeft = hardwareMap.get(DcMotorEx.class, DriveConstants.FRONT_LEFT_NAME);
        frontRight = hardwareMap.get(DcMotorEx.class, DriveConstants.FRONT_RIGHT_NAME);
        backLeft = hardwareMap.get(DcMotorEx.class, DriveConstants.BACK_LEFT_NAME);
        backRight = hardwareMap.get(DcMotorEx.class, DriveConstants.BACK_RIGHT_NAME);

        frontLeft.setDirection(DriveConstants.FRONT_LEFT_REVERSED
                ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backLeft.setDirection(DriveConstants.BACK_LEFT_REVERSED
                ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        frontRight.setDirection(DriveConstants.FRONT_RIGHT_REVERSED
                ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
        backRight.setDirection(DriveConstants.BACK_RIGHT_REVERSED
                ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);

        for (DcMotorEx motor : new DcMotorEx[] {frontLeft, frontRight, backLeft, backRight}) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        imu = hardwareMap.get(IMU.class, DriveConstants.IMU_NAME);
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
                DriveConstants.LOGO_DIRECTION, DriveConstants.USB_DIRECTION)));
        imu.resetYaw();
    }

    // ========================================================================
    // 1. TeleOp driving — continuous power, driver-controlled
    // ========================================================================

    /**
     * Drives with field-centric translation and robot-centric turn: pushing
     * the stick away from the driver moves the robot away from the driver,
     * regardless of which way it's currently facing.
     *
     * @param forward  +1 away from the driver
     * @param strafe   +1 to the driver's right
     * @param turn     +1 clockwise, viewed from above
     * @param slowMode true to scale everything down for fine control
     */
    public void driveFieldCentric(double forward, double strafe, double turn, boolean slowMode) {
        lastHeadingRadians = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the driver's stick vector into the robot's frame by -heading.
        double rotatedForward = strafe * Math.sin(-lastHeadingRadians) + forward * Math.cos(-lastHeadingRadians);
        double rotatedStrafe = strafe * Math.cos(-lastHeadingRadians) - forward * Math.sin(-lastHeadingRadians);

        drive(rotatedForward, rotatedStrafe * DriveConstants.STRAFE_CORRECTION, turn, slowMode);
    }

    /** Drives relative to the robot itself, ignoring heading. Fallback if the IMU misbehaves. */
    public void driveRobotCentric(double forward, double strafe, double turn, boolean slowMode) {
        drive(forward, strafe * DriveConstants.STRAFE_CORRECTION, turn, slowMode);
    }

    private void drive(double forward, double strafe, double turn, boolean slowMode) {
        double speed = slowMode ? DriveConstants.SLOW_SPEED : DriveConstants.NORMAL_SPEED;

        // Scale by the largest combined command rather than clipping each
        // wheel separately, which would distort the direction of travel.
        double denominator = Math.max(Math.abs(forward) + Math.abs(strafe) + Math.abs(turn), 1.0);

        frontLeft.setPower(((forward + strafe + turn) / denominator) * speed);
        backLeft.setPower(((forward - strafe + turn) / denominator) * speed);
        frontRight.setPower(((forward - strafe - turn) / denominator) * speed);
        backRight.setPower(((forward + strafe - turn) / denominator) * speed);
    }

    /** Zeroes the heading reference — "whichever way the robot faces now" becomes forward. */
    public void resetHeading() {
        imu.resetYaw();
    }

    /** @return heading in degrees, updated each time a drive*() method runs. */
    public double getHeadingDegrees() {
        return Math.toDegrees(lastHeadingRadians);
    }

    // ========================================================================
    // 2. Autonomous primitives — one step of work each, no blocking.
    //
    // The calling OpMode is responsible for looping until the move is done or
    // has timed out. That loop's shape:
    //
    //   drive.startDriveInches(24, DriveConstants.AUTO_DRIVE_POWER);
    //   ElapsedTime timer = new ElapsedTime();
    //   while (opModeIsActive() && drive.isBusyDriving()
    //           && timer.seconds() < timeoutSeconds) { /* just wait */ }
    //   drive.stopAndResetForTeleOp();
    // ========================================================================

    /** Starts driving straight forward (positive) or backward (negative) a distance in inches. */
    public void startDriveInches(double inches, double power) {
        int delta = (int) Math.round(inches * DriveConstants.COUNTS_PER_INCH);
        setEncoderTargets(delta, delta, delta, delta, power);
    }

    /** Starts strafing right (positive) or left (negative) a distance in inches. */
    public void startStrafeInches(double inches, double power) {
        int delta = (int) Math.round(inches * DriveConstants.COUNTS_PER_INCH * DriveConstants.STRAFE_CORRECTION);
        // Same sign pattern as the strafe term in drive(): FL/BR one way, FR/BL the other.
        setEncoderTargets(delta, -delta, -delta, delta, power);
    }

    private void setEncoderTargets(int flDelta, int frDelta, int blDelta, int brDelta, double power) {
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + flDelta);
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + frDelta);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + blDelta);
        backRight.setTargetPosition(backRight.getCurrentPosition() + brDelta);

        for (DcMotorEx motor : new DcMotorEx[] {frontLeft, frontRight, backLeft, backRight}) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        // RUN_TO_POSITION direction comes from target vs. current position, not
        // the sign of power — so every motor gets the same positive magnitude.
        double magnitude = Math.abs(power);
        frontLeft.setPower(magnitude);
        frontRight.setPower(magnitude);
        backLeft.setPower(magnitude);
        backRight.setPower(magnitude);
    }

    /** @return true while any wheel is still running to its RUN_TO_POSITION target. */
    public boolean isBusyDriving() {
        return frontLeft.isBusy() || frontRight.isBusy() || backLeft.isBusy() || backRight.isBusy();
    }

    /**
     * Applies one step of proportional turning toward an absolute heading
     * (degrees, measured from wherever {@link #resetHeading()} was last
     * called). Call this every loop iteration until {@link #isAtHeading}
     * returns true — see the class-level example.
     */
    public void applyTurnCorrection(double targetHeadingDegrees) {
        double error = headingErrorDegrees(targetHeadingDegrees);
        double turnPower = Math.max(-0.5, Math.min(0.5, error * DriveConstants.TURN_GAIN));
        driveRobotCentric(0, 0, turnPower, false);
    }

    /** @return true once the heading is within {@link DriveConstants#HEADING_TOLERANCE_DEGREES}. */
    public boolean isAtHeading(double targetHeadingDegrees) {
        return Math.abs(headingErrorDegrees(targetHeadingDegrees)) < DriveConstants.HEADING_TOLERANCE_DEGREES;
    }

    private double headingErrorDegrees(double targetHeadingDegrees) {
        double currentDegrees = Math.toDegrees(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        double error = targetHeadingDegrees - currentDegrees;
        // Normalise into (-180, 180] so the robot always turns the short way.
        while (error > 180) error -= 360;
        while (error <= -180) error += 360;
        return error;
    }

    /** Stops all motion and returns the motors to normal power-based driving. */
    public void stop() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    /** Call after an autonomous move before switching back to TeleOp-style driving. */
    public void stopAndResetForTeleOp() {
        stop();
        for (DcMotorEx motor : new DcMotorEx[] {frontLeft, frontRight, backLeft, backRight}) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    public void outputTelemetry(Telemetry telemetry) {
        telemetry.addData("--- Drive ---", "");
        telemetry.addData("Heading (deg)", "%.1f", getHeadingDegrees());
        telemetry.addData("FL / FR ticks", "%d / %d", frontLeft.getCurrentPosition(), frontRight.getCurrentPosition());
        telemetry.addData("BL / BR ticks", "%d / %d", backLeft.getCurrentPosition(), backRight.getCurrentPosition());
    }
}
