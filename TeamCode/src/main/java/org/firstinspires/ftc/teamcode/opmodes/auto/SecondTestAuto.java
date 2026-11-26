package org.firstinspires.ftc.teamcode.opmodes.auto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.interpolator.Interpolator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;


@Autonomous (name="SecondTestAuto")
public class SecondTestAuto extends OpMode {
    private Follower follower;
    private Flywheel flywheel;
    private final PoseFactory p = PoseFactory.degrees();

    // HARDWARE
    private DcMotorEx flywheelMotorOne,
            flywheelMotorTwo,
            intakeMotor;

    private static final double NEAR_RPM = 1200;
    private static final double FAR_RPM  = 1800;

    private static final double LAUNCH_POWER = 0.5;



    ///left and right are relative to the driver
    /*

     */
    public final Pose shoot_right = p.of(56, 9, 180); //also start
    public final Pose shoot_left = p.of(9,132, 270);
    public final Pose loading_zone = p.of(9, 104, 90);
    public final Pose garden = p.of(14,9, 180);

    public final Pose shootToGardenControl = p.of(36,20,180);
    public final Pose park = p.of(20,90,90);

    private Path rightToLoad = curve(shoot_right, new Pose(33, 28), loading_zone).linear(shoot_right, loading_zone);
    private Path gardenToPark = curve(garden, park).linear(garden, park);
    private Path rightToGarden = curve(shoot_right, shootToGardenControl, garden).linear(shoot_right, garden);
    private Path gardenToRight = curve(garden, new Pose(33, 28), shoot_right).linear(shoot_right, garden);
    private Path leftToGarden = line(shoot_left, garden);
    private Path gardenToLeft = line(garden, shoot_left);

    private Command autoRoutine() {
        return sequential(
                // insert launch here
                shooter_launch,
                follow(follower, rightToGarden),
                shooter_position(0),
                // insert pollen intake here
                follow(follower, gardenToPark)
        );
    }

    Command shooter_launch = Command.build()
            .setStart(() -> {
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                flywheelMotorOne.setPower(LAUNCH_POWER);
                flywheelMotorTwo.setPower(LAUNCH_POWER);
            })
            .setDone(() -> flywheelMotorOne.getCurrentPosition()  > 1000)
            .setEnd(endCondition -> {
                flywheelMotorOne.setPower(0);
                flywheelMotorTwo.setPower(0);
            })
            .requiring(flywheelMotorOne);

    public Command shooter_position(double target) {
        return Command.build()
                .setStart(() -> {
                    flywheelMotorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    flywheelMotorOne.setPower(LAUNCH_POWER);
                    flywheelMotorTwo.setPower(LAUNCH_POWER);
                })
                .setDone(() -> ((flywheelMotorOne.getCurrentPosition() > target - 100) && (flywheelMotorOne.getCurrentPosition() < target + 100)))
                .setEnd(endCondition -> {
                   flywheelMotorOne.setPower(0);
                   flywheelMotorTwo.setPower(0);
                })
                .requiring(flywheelMotorOne);
    } // end of command shooter_position



    @Override
    public void init() {
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(shoot_right);
        follower.update();

        // init HARDWARE
        flywheelMotorOne = hardwareMap.get(DcMotorEx.class, "flywheelMotorOne");
        flywheelMotorOne.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheelMotorTwo = hardwareMap.get(DcMotorEx.class, "flywheelMotorTwo");
        flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
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

}

