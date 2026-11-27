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
import com.pedropathing.paths.interpolator.Interpolator;
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
    private Park park = Park.BOTTOM;
    public boolean partner = false;
    public double delay = 0.0;
    public final Pose start_right = p.of(56, 9, 0);
    public final Pose start_partner = p.of(32, 9, 0);
    public final Pose shoot_right = p.of(58, 12, 0); //also start
    public final Pose shoot_left = p.of(9,132, Math.PI);
    public final Pose loading_zone_close = p.of(9, 91, Math.PI/2); //centeredish is 9,104
    public final Pose loading_zone_far = p.of(9, 123, Math.PI/2);
    public final Pose garden = p.of(9,9, Math.PI);
    public Pose under_hive = p.of(59, 64, 0);

    public Pose start_position = start_right;


    private final Path startToHive = curve(start_partner, new Pose(24, 58), new Pose(66, 13), under_hive).linear(shoot_right.heading(), under_hive.heading());
    private final Path hiveToShoot = line(under_hive, shoot_right).linear(under_hive, shoot_right);
    private final Path startToShootBasic = line(start_position, shoot_right).linear(start_position, shoot_right);
    private final Path rightToLoad = curve(shoot_right, new Pose(33, 28), loading_zone_close).linear(shoot_right.heading(), loading_zone_close.heading());
    private final Path rightToFarLoad = curve(shoot_right, new Pose(58, 115), new Pose(66, 121), loading_zone_far)
            .heading(Interpolator.piecewise().until(0.2, Interpolator.linear(0, Math.PI/2))
                    .until(1.0, Interpolator.constant(Math.PI/2)));
    private final Path rightToGarden = curve(shoot_right, new Pose(33, 28), garden).linear(shoot_right, garden);
    private final Path gardenToRight = curve(garden, new Pose(33, 28), shoot_right).linear(shoot_right, garden);
    private final Path leftToGarden = line(shoot_left, garden);
    private final Path gardenToLeft = line(garden, shoot_left);


//    private Command localShootThree() {
//        return
//    }

    private Command goPark() {
        if (park == Park.BOTTOM) {
            return follow(follower, rightToLoad);
        } else {
            return follow(follower, rightToFarLoad);
        }
    }

    private Command goShoot() {
        if (partner) {
            return sequential(
                    follow(follower, startToHive),
                    waitMs(delay),
                    follow(follower, hiveToShoot),
                    waitMs(200));
        } else {
            return follow(follower, startToShootBasic);
        }
    }

    private Command autoRoutine() {
        return sequential(
                goShoot(),
                waitMs(250),
                flywheel.shootThree(2400.0),
                waitMs(200),
                waitMs(5000),
                goPark()
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

        if (gamepad1.leftBumperWasPressed()) {
            delay+= 250;
        } else if (gamepad1.rightBumperWasPressed()) {
            delay -= 250;
        }
        telemetry.addData("Delay before shooting", delay/1000+"s");

        telemetry.update();
    }

    @Override
    public void start() {
        follower.setPose(start_position);
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