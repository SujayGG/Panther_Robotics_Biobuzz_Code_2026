package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

/**
 * Everything about the drivetrain that's specific to THIS robot, in one place.
 *
 * <p>The chassis this was written against is not the chassis you're running —
 * different wheel size, different motors, different weight. None of that
 * requires touching {@link org.firstinspires.ftc.teamcode.subsystems.MecanumDrive}
 * or either OpMode in {@code opmodes/}; it only requires filling in the
 * numbers below correctly for your robot. That's the point of keeping them
 * here instead of scattered through the drive code.
 *
 * <p>Two constants below are physical measurements you must get right before
 * autonomous distances mean anything: {@link #COUNTS_PER_MOTOR_REV} and
 * {@link #WHEEL_DIAMETER_INCHES}. Everything else has a sane default.
 */
public final class DriveConstants {

    private DriveConstants() {} // constants only, never instantiated

    // ========================================================================
    // Hardware names — must match the robot configuration on the Driver
    // Station EXACTLY. These are guesses; fix them before anything else.
    // ========================================================================
    public static final String FRONT_LEFT_NAME = "frontLeft";
    public static final String FRONT_RIGHT_NAME = "frontRight";
    public static final String BACK_LEFT_NAME = "backLeft";
    public static final String BACK_RIGHT_NAME = "backRight";
    public static final String IMU_NAME = "imu";

    // ========================================================================
    // Motor encoder — REQUIRED, robot-specific.
    //
    // Find your motor on its vendor's page and look for "encoder counts per
    // revolution" or "CPR". Common FTC drivetrain motors:
    //
    //   goBILDA 5203 series,  312 RPM (most common FTC drive motor) → 537.7
    //   goBILDA 5203 series,  435 RPM                               → 384.5
    //   goBILDA 5203 series,  223 RPM                                → 751.8
    //   goBILDA 5203 series, 1150 RPM (no gearbox)                   →  28.0
    //   REV HD Hex motor,      40:1                                  → 2240.0
    //   REV HD Hex motor,      20:1                                  → 1120.0
    //   Tetrix Motor with standard encoder                           → 1440.0
    //
    // Using the wrong value doesn't break the build — it silently makes every
    // autonomous distance wrong by a fixed ratio. If the robot consistently
    // drives short or long by the same proportion, check this number first.
    // ========================================================================
    public static final double COUNTS_PER_MOTOR_REV = 537.7; // goBILDA 312 RPM — CHANGE FOR YOUR MOTOR

    /** External gearing between motor and wheel, beyond the motor's own gearbox. 1.0 = none. */
    public static final double DRIVE_GEAR_REDUCTION = 1.0;

    // ========================================================================
    // Wheel diameter — REQUIRED, robot-specific. Measure the actual wheel,
    // don't trust a spec sheet; manufacturing tolerance is real at this scale.
    //
    //   goBILDA 96mm mecanum  → 3.78
    //   goBILDA 75mm mecanum  → 2.95
    //   "4 inch" mecanum      → 4.0
    // ========================================================================
    public static final double WHEEL_DIAMETER_INCHES = 3.78; // goBILDA 96mm — CHANGE FOR YOUR WHEELS

    /** Derived — don't edit. Encoder counts per inch the wheel actually rolls forward. */
    public static final double COUNTS_PER_INCH =
            (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * Math.PI);

    /**
     * Mecanum wheels scrub sideways and lose more to friction strafing than
     * driving forward, so the same encoder count covers less real ground
     * strafing. This inflates the strafe target so the robot travels the
     * distance you actually asked for.
     *
     * <p>Tune it by strafing a measured distance (say 24 in) and comparing to
     * what the robot really travelled: {@code correction = requested / actual}.
     * Expect somewhere around 1.0–1.3. This robot's number will differ from
     * the one this file shipped with — that's expected, it's not tuned yet.
     */
    public static final double STRAFE_CORRECTION = 1.1;

    // ========================================================================
    // Motor directions. Verify on the real robot: push both sticks forward in
    // TeleOp and confirm all four wheels spin the same way — not by assuming
    // these are already right for your build.
    // ========================================================================
    public static final boolean FRONT_LEFT_REVERSED = true;
    public static final boolean BACK_LEFT_REVERSED = true;
    public static final boolean FRONT_RIGHT_REVERSED = false;
    public static final boolean BACK_RIGHT_REVERSED = false;

    // ========================================================================
    // How the Control Hub is physically bolted to the robot. Go look at the
    // robot — don't guess. Wrong values give wrong headings, which makes
    // field-centric drive and every turnDegrees() call subtly wrong.
    // ========================================================================
    public static final RevHubOrientationOnRobot.LogoFacingDirection LOGO_DIRECTION =
            RevHubOrientationOnRobot.LogoFacingDirection.UP;
    public static final RevHubOrientationOnRobot.UsbFacingDirection USB_DIRECTION =
            RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;

    // ========================================================================
    // TeleOp speeds
    // ========================================================================
    public static final double NORMAL_SPEED = 1.0;
    public static final double SLOW_SPEED = 0.35;

    // ========================================================================
    // Autonomous tuning
    // ========================================================================
    /** Power used while an encoder-driven move is still far from its target. */
    public static final double AUTO_DRIVE_POWER = 0.5;

    /** Proportional gain for IMU-corrected turning. Start small; raise until
     *  turns arrive briskly without overshooting and oscillating. */
    public static final double TURN_GAIN = 0.02;

    /** Stop turning once within this many degrees of the target heading. */
    public static final double HEADING_TOLERANCE_DEGREES = 1.0;

    /** Safety cap so a stuck target never spins the robot forever in auto. */
    public static final double TURN_TIMEOUT_SECONDS = 3.0;
}
