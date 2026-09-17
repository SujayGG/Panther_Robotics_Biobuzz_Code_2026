package org.firstinspires.ftc.teamcode.pedro;

// ============================================================================
//  TEMPLATE — copy into TeamCode/.../teamcode/pedro/ after the Gradle step in
//  docs/guides/02-odometry-pedro-pathing.md section 4.
//
//  Purpose: the first thing you run after tuning. Drive the robot around and
//  watch its position on telemetry. If these numbers are wrong, nothing you
//  build on top of odometry can work — so verify here first.
// ============================================================================

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Pedro TeleOp", group = "Competition")
public class PedroTeleOp extends OpMode {

    private Follower follower;

    @Override
    public void init() {
        // TODO 1: Build the follower from your tuned Constants.
        //   follower = Constants.create(hardwareMap);

        // TODO 2: Tell Pedro where the robot is. For a TeleOp used to check
        // odometry, starting at the origin is convenient — then "drive 24
        // inches forward" should read x = 24.
        //   follower.setPose(new Pose(0, 0, 0));

        telemetry.addData("Status", "Initialised");
        telemetry.update();
    }

    @Override
    public void loop() {
        // ------------------------------------------------------------------
        // TODO 3: Hand the sticks to Pedro.
        //
        // follower.manual(forward, lateral, heading) replaces all the mecanum
        // kinematics from Guide 1 — Pedro's Mecanum drivetrain does that work.
        //
        //   follower.manual(
        //           -gamepad1.left_stick_y,
        //           -gamepad1.left_stick_x,
        //           -gamepad1.right_stick_x);
        //
        // Check every sign against the real robot on the first run. Which way
        // is positive depends on your pod directions and how the hub is
        // mounted, so there is no universally correct set of signs here.
        // ------------------------------------------------------------------

        // ------------------------------------------------------------------
        // TODO 4: follower.update();
        //
        // Reads odometry, then sets motor powers. Miss it and the robot simply
        // does not move, with no error to explain why. This is the single most
        // common Pedro mistake.
        // ------------------------------------------------------------------

        // TODO 5: Print the pose. This is the whole point of this OpMode.
        //   telemetry.addData("x", "%.1f", follower.pose().x());
        //   telemetry.addData("y", "%.1f", follower.pose().y());
        //   telemetry.addData("heading", "%.1f",
        //           Math.toDegrees(follower.pose().heading()));

        telemetry.update();
    }
}
