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
public class leftPark extends OpMode {
    private Follower follower;
    private Flywheel flywheel;
    private final PoseFactory p = PoseFactory.radians();

    ///left and right are relative to the driver
    enum Park {
        TOP,
        BOTTOM
    }
    private RightPark.Park park = RightPark.Park.BOTTOM;
    public boolean partner = false;
    public double delay = 0.0;
    public final Pose start_left = p.of(58, 132, Math.PI);
    public final Pose shoot_left = p.of(58, 127, Math.PI);
    public final Pose loading_zone_left = p.of(10, 123, Math.PI/2);
    public final Pose loading_zone_right = p.of(10, 91, 3*Math.PI/2); //centeredish is 9,104
    public final Pose garden = p.of(9,9, Math.PI);


    private final Path leftToGarden = line(start_left, garden);
    private final Path gardenToLeft = line(garden, start_left);
    private final Path startToShoot = line(start_left, shoot_left).linear(start_left, shoot_left);
    private final Path shootToLoad = line(shoot_left, loading_zone_left).linear(shoot_left, loading_zone_left);


//    private Command localShootThree() {
//        return
//    }



    private Command autoRoutine() {
        return sequential(
                waitMs(delay),
                follow(follower, startToShoot),
                waitMs(250),
                flywheel.shootThree(2660.0),
                waitMs(200),
                follow(follower, shootToLoad)
        ).with(flywheel.periodic());
    }

    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);

        flywheel = new Flywheel(this);
    }

    @Override
    public void start() {
        follower.setPose(start_left);
        follower.update();
        Scheduler.schedule(autoRoutine());
    }

    @Override
    public void init_loop() {
        if (gamepad1.leftBumperWasPressed()) {
            delay+= 250;
        } else if (gamepad1.rightBumperWasPressed()) {
            delay -= 250;
        }
        telemetry.addData("Delay before shooting", delay/1000+"s");

        telemetry.update();
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