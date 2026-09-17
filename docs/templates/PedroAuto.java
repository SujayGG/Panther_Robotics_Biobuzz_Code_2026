package org.firstinspires.ftc.teamcode.pedro;

// ============================================================================
//  TEMPLATE — copy into TeamCode/.../teamcode/pedro/ after the Gradle step in
//  docs/guides/02-odometry-pedro-pathing.md section 4.
//
//  A state-machine autonomous. The structure is the lesson: autonomous cannot
//  block, so instead of waiting for the robot to arrive you check "am I done
//  yet?" on every loop and advance when you are.
//
//  Convention below: EVEN states start an action, ODD states wait for it.
// ============================================================================

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "BIOBUZZ Auto", group = "Competition")
public class PedroAuto extends OpMode {

    private Follower follower;
    private int state = 0;

    // PoseFactory.degrees() lets you write headings in degrees while Pedro
    // works in radians internally. Swap in .mirrorX(72) for the other
    // alliance and every pose below flips with it — 72 is half of the
    // 144-inch field.
    private final PoseFactory field = PoseFactory.degrees();

    private Pose startPose, scorePose, parkPose;

    @Override
    public void init() {
        // TODO 1: follower = Constants.create(hardwareMap);

        // ------------------------------------------------------------------
        // TODO 2: Define your poses, in inches, as field.of(x, y, headingDeg).
        //
        // Measure these against the real BIOBUZZ field — read the Competition
        // Manual and work out where you actually want the robot. The numbers
        // below are placeholders to show the shape, not a working auto.
        // ------------------------------------------------------------------
        startPose = field.of(9, 60, 0);
        scorePose = field.of(36, 84, 45);
        parkPose = field.of(60, 96, 90);

        // ------------------------------------------------------------------
        // TODO 3: follower.setPose(startPose);
        //
        // Pedro has no idea where you physically placed the robot. If this
        // does not match reality, EVERY path is offset by the same error and
        // the auto will look mysteriously wrong. Build a physical alignment
        // jig so the robot starts identically every match.
        // ------------------------------------------------------------------

        telemetry.addData("Status", "Initialised — check the robot is on its mark");
        telemetry.update();
    }

    @Override
    public void loop() {
        // TODO 4: follower.update();  — first line, every loop, no exceptions.

        switch (state) {
            case 0:
                // TODO 5: Start driving to the scoring position.
                //   follower.follow(Paths.line(startPose, scorePose));
                state = 1;
                break;

            case 1:
                // TODO 6: Wait for arrival, then score.
                //   if (!follower.isBusy()) {
                //       // run your scoring mechanism here
                //       state = 2;
                //   }
                break;

            case 2:
                // TODO 7: Drive to park.
                //   follower.follow(Paths.line(scorePose, parkPose));
                state = 3;
                break;

            case 3:
                // TODO 8: if (!follower.isBusy()) state = 4;   // done
                break;

            default:
                // Parked. Nothing left to do.
                break;
        }

        telemetry.addData("State", state);
        // TODO 9: telemetry.addData("Pose", follower.pose());
        //         telemetry.addData("Completion", "%.0f%%", follower.completion() * 100);
        telemetry.update();
    }
}
