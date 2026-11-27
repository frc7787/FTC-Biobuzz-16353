package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.control.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;


@Autonomous
public class RightPark extends OpMode {
    private Follower follower;
    private Flywheel flywheel;
    private final PoseFactory p = PoseFactory.degrees();

    ///left and right are relative to the driver
    enum Park {
        TOP,
        BOTTOM
    }
    public Park park = Park.BOTTOM;
    public boolean partner = false;
    public double delay = 0.0;
    public final Pose start_right = p.of(56, 9, 0);
    public final Pose start_partner = p.of(32, 9, 0);
    public final Pose shoot_right = p.of(58, 12, 0); //also start
    public final Pose shoot_left = p.of(9,132, Math.PI);
    public final Pose loading_zone = p.of(9, 95, Math.PI/2); //centeredish is 9,104
    public final Pose garden = p.of(9,9, Math.PI);

    public Pose start_position = start_right;

    private final Path startToShoot = line(start_right, shoot_right).constant(start_right);
    private final Path rightToLoad = curve(shoot_right, new Pose(33, 28), loading_zone).linear(shoot_right, garden);
    private final Path rightToGarden = curve(shoot_right, new Pose(33, 28), garden).linear(shoot_right, garden);
    private final Path gardenToRight = curve(garden, new Pose(33, 28), shoot_right).linear(shoot_right, garden);
    private final Path leftToGarden = line(shoot_left, garden);
    private final Path gardenToLeft = line(garden, shoot_left);


//    private Command localShootThree() {
//        return
//    }
    private Command autoRoutine() {
        if (partner) {
            return sequential(
                    waitMs(delay),
                    follow(follower, startToShoot),
                    flywheel.shootThree(2400.0),
                    waitMs(200)
            ).with(flywheel.periodic());
        } else {
            return sequential(
                    waitMs(delay),
                    follow(follower, startToShoot),
                    flywheel.shootThree(2400.0),
                    waitMs(200)
            ).with(flywheel.periodic());
        }
    }


    private Command autoRoutine() {
        return sequential(
                waitMs(delay),
                follow(follower, startToShoot),
                flywheel.shootThree(2400.0),
                waitMs(200),
                follow(follower, rightToLoad)
        ).with(flywheel.periodic());
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);

        flywheel = new Flywheel(this);
    }

    @Override
    public void init_loop() {
        if (gamepad1.dpadUpWasPressed()) {
            park = Park.TOP;
        } else if (gamepad1.dpadDownWasPressed()) {
            park = Park.BOTTOM;
        }
        telemetry.addData("(dpadU/D) parking position", park);

        if (gamepad1.dpadRightWasPressed()) {
            partner = !partner;
            if (partner) {
                start_position = start_partner;
            } else {
                start_position = start_right;
            }
        }
        telemetry.addData("(dpadR) Partner start position", partner);

        if (gamepad1.left_bumper) {
            delay+= 100;
        } else if (gamepad1.right_bumper) {
            delay -= 100;
        }
        telemetry.addData("Delay before starting", delay/100+"s");

        telemetry.update();
    }

    @Override
    public void start() {
        follower.setPose(start_right);
        follower.update();
        Scheduler.schedule(autoRoutine());
    }


    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
    }

    @Override
    public void stop() {
        OpModeStorage.autonomousEndPose = follower.pose(); //saves your position in that file
    }
}