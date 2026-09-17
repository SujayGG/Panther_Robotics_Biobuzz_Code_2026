package org.firstinspires.ftc.teamcode.exercises;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/**
 * EXERCISE 3 — Field-centric mecanum drive.
 *
 * <p>Goal: push the stick away from yourself and the robot drives away from
 * you — regardless of which way the robot is pointing. Once drivers try this
 * they never want robot-centric back, because they no longer have to do the
 * mental rotation when the robot is facing them.
 *
 * <p>The trick is one rotation: take the driver's stick vector and rotate it
 * by minus the robot's heading, which the IMU gives you. Then feed the result
 * into the exact same mecanum kinematics from Exercise 2.
 *
 * <p>Read guides/01-teleop-mecanum.md sections 8–10 before starting.
 */
@TeleOp(name = "Ex3: Field Centric", group = "Exercises")
public class Exercise3_FieldCentric extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private IMU imu;

    @Override
    public void runOpMode() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // TODO 1: Reverse the left side, as in Exercise 2.

        // ================================================================
        // TODO 2: Set up the IMU.
        //
        // These two directions describe how the Control Hub is physically
        // bolted to the robot. Get them wrong and your headings are wrong,
        // which makes field-centric drive behave bizarrely — so go look at
        // the robot rather than guessing.
        //
        // LogoFacingDirection  = which way the REV logo points
        // UsbFacingDirection   = which way the USB ports point
        //
        //   imu = hardwareMap.get(IMU.class, "imu");
        //   imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(
        //           RevHubOrientationOnRobot.LogoFacingDirection.UP,
        //           RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));
        //   imu.resetYaw();
        // ================================================================
        imu = hardwareMap.get(IMU.class, "imu");

        telemetry.addData("Status", "Ready — press PLAY");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ============================================================
            // TODO 3: Let the driver re-zero the heading.
            //
            // Whatever direction the robot faces when you reset becomes the
            // new "forward". Drivers need this after a collision spins the
            // robot, so bind it to a button they can find without looking:
            //
            //   if (gamepad1.back) {
            //       imu.resetYaw();
            //   }
            // ============================================================

            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            // ============================================================
            // TODO 4: Read the heading, in RADIANS.
            //
            // Math.sin and Math.cos take radians, not degrees. Asking for
            // degrees here and feeding them to cos() is a classic bug that
            // produces drive that is subtly, maddeningly wrong.
            //
            //   double heading = imu.getRobotYawPitchRollAngles()
            //                       .getYaw(AngleUnit.RADIANS);
            // ============================================================
            double heading = 0;

            // ============================================================
            // TODO 5: Rotate the stick vector by -heading.
            //
            // This is the standard 2D rotation matrix. The minus sign is the
            // whole point: the robot has turned +heading away from the field,
            // so to express the driver's field-frame request in the robot's
            // frame you rotate the other way.
            //
            //   double rotStrafe  = strafe * Math.cos(-heading)
            //                     - forward * Math.sin(-heading);
            //   double rotForward = strafe * Math.sin(-heading)
            //                     + forward * Math.cos(-heading);
            //
            // Check it by hand: at heading = 90° (robot turned left), pushing
            // the stick away from you should make the robot STRAFE, not drive.
            // ============================================================
            double rotForward = forward;
            double rotStrafe = strafe;

            // Mecanum wheels strafe less efficiently than they drive, so
            // sideways input gets a boost. Tune this number by strafing a
            // measured distance and comparing it to driving the same distance.
            rotStrafe *= 1.1;

            // TODO 6: Same kinematics and normalisation as Exercise 2, but
            // using rotForward and rotStrafe instead of forward and strafe.
            double frontLeftPower = 0;
            double backLeftPower = 0;
            double frontRightPower = 0;
            double backRightPower = 0;

            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            telemetry.addData("Heading (deg)", "%.1f", Math.toDegrees(heading));
            telemetry.addData("FL / FR", "%.2f / %.2f", frontLeftPower, frontRightPower);
            telemetry.addData("BL / BR", "%.2f / %.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }
}
