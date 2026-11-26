package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
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


@Autonomous(group = "debug")
public class returnToStart extends OpMode {
    private Follower follower;
    private Flywheel flywheel;
    private final PoseFactory p = PoseFactory.degrees();

    ///left and right are relative to the driver

    public final Pose shoot_right = p.of(56, 9, Math.PI/2); //also start
    public final Pose shoot_left = p.of(9,132, 3*Math.PI/2);
    public final Pose loading_zone = p.of(9, 104, Math.PI);
    public final Pose garden = p.of(9,9, 3*Math.PI/2);

    private final Path LoadToRight = curve(loading_zone, new Pose(33, 28), shoot_right).linear(shoot_right, garden);
    private final Path rightToGarden = curve(shoot_right, new Pose(33, 28), garden).linear(shoot_right, garden);
    private final Path gardenToRight = curve(garden, new Pose(33, 28), shoot_right).linear(shoot_right, garden);
    private final Path leftToGarden = line(shoot_left, garden);
    private final Path gardenToLeft = line(garden, shoot_left);

    private Command autoRoutine() {
        return parallel(
                sequential(
                        flywheel.shootThree(2400.0),
                        waitMs(200),
                        follow(follower, LoadToRight)
                ), flywheel.periodic());
    }
    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(loading_zone);
        follower.update();

        flywheel = new Flywheel(this);
    }

    @Override
    public void start() {
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