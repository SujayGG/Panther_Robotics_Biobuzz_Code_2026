package org.firstinspires.ftc.teamcode.pedro;

// ============================================================================
//  PEDRO PATHING 3 — CONFIGURATION TEMPLATE
//
//  This file is a TEMPLATE. It lives in docs/templates/ so it does not break
//  the build. Copy it to:
//
//      TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pedro/
//
//  ONLY AFTER you have done the Gradle step in
//  docs/guides/02-odometry-pedro-pathing.md section 4. Before that, none of
//  these imports resolve.
//
//  Then work top to bottom. Every value marked "from <tuner>" must come from
//  running that tuner on YOUR robot. Do not invent them and do not copy them
//  from another team — they are physical measurements of this robot.
// ============================================================================

import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // ------------------------------------------------------------------
    // STEP 1 — Drivetrain.   Run: MecanumTuner
    //
    // The four names must match the robot configuration exactly. The four
    // directions come from MecanumTuner, which spins each wheel in turn and
    // asks you which way it went.
    // ------------------------------------------------------------------
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");        // TODO match robot config
        c.frontRightName.set("frontRight");      // TODO
        c.backLeftName.set("backLeft");          // TODO
        c.backRightName.set("backRight");        // TODO

        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);   // TODO from MecanumTuner
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);  // TODO
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);    // TODO
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);   // TODO
    });

    // ------------------------------------------------------------------
    // STEP 2 — Localizer.   Run: PinpointTuner
    //
    // Using something other than a Pinpoint? Swap PinpointConfig for
    // OTOSConfig / ThreeWheelConfig / TwoWheelConfig / OctoQuadConfig and run
    // that localizer's tuner instead. The rest of this file is unchanged.
    //
    // The offsets describe where the pods sit relative to the robot's centre
    // of rotation, in inches. PinpointTuner finds them by having you spin the
    // robot 180 degrees.
    // ------------------------------------------------------------------
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");                  // TODO match robot config

        // goBILDA_4_BAR_POD or goBILDA_SWINGARM_POD — whichever you own.
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        c.xPodOffset.set(0.0);                   // TODO from PinpointTuner
        c.yPodOffset.set(0.0);                   // TODO from PinpointTuner

        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);  // TODO
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);  // TODO

        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    // ------------------------------------------------------------------
    // STEP 3 — Foresight (the path follower).   Run: ForesightTuner
    //
    // Deliberately left commented out. Every field below is REQUIRED and has
    // no default, because each one is a measurement of how your robot
    // accelerates, coasts and brakes. ForesightTuner measures them and prints
    // a finished block — paste it here, replacing this comment entirely.
    //
    // Guessing these produces a robot that drives into walls at speed.
    //
    // The tuner's output looks like:
    //
    // public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
    //     Controller primaryForward   = Controller.proportional(...);
    //     Controller secondaryForward = Controller.proportional(...);
    //     Controller primaryLateral   = Controller.proportional(...);
    //     Controller secondaryLateral = Controller.proportional(...);
    //
    //     c.forwardTranslational.set(
    //             Controller.piecewise(secondaryForward).put(2.5, primaryForward));
    //     c.strafeTranslational.set(
    //             Controller.piecewise(secondaryLateral).put(2.5, primaryLateral));
    //
    //     c.coast.set(Controller.proportionalFeedforward(...));
    //     c.brake.set(Controller.proportionalFeedforward(...));
    //
    //     c.headingFeedback.set(Controller.proportional(...));
    //     c.headingBrakeCoefficients.set(Vector2D.cartesian(..., ...));
    //
    //     c.linearBrakeCoefficients.set(Matrix.diag(..., ...));
    //     c.quadraticBrakeCoefficients.set(Matrix.diag(..., ...));
    //
    //     c.maxAchievableForwardVelocity.set(...);
    //     c.maxAchievableStrafeVelocity.set(...);
    //     c.naturalForwardDeceleration.set(...);
    //     c.naturalStrafeDeceleration.set(...);
    // });
    // ------------------------------------------------------------------

    /**
     * Builds the Follower every OpMode uses.
     *
     * <p>Argument order is {@code (Localizer, Drivetrain, Algorithm)} — checked
     * against the Pedro v3.0.0 source. A comment in the official Quickstart
     * lists it the other way around; ignore that. Since all three are distinct
     * types, a mistake here is a compile error rather than a silent bug.
     */
    public static Follower create(HardwareMap h) {
        // ------------------------------------------------------------------
        // STEP 4 — Once foresightConfig above is filled in, delete the
        // `return null;` and uncomment the real return.
        //
        // Leaving it as null means any OpMode calling this will throw a
        // NullPointerException immediately — which is the point. Better an
        // obvious crash on INIT than a robot that drives somewhere random.
        // ------------------------------------------------------------------
        return null;

        // return new Follower(
        //         new PinpointLocalizer(h, localizerConfig),
        //         new Mecanum(h, drivetrainConfig),
        //         new Foresight(foresightConfig));
    }
}
