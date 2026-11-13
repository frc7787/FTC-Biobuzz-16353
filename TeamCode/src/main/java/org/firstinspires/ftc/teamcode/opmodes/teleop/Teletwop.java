package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robot;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

@TeleOp(name = "Teletwop")
public class Teletwop extends OpMode {
    private Follower follower;

    private DcMotorEx flywheelMotorOne,
            flywheelMotorTwo,
            intakeMotor,
            flippyMotor;


    @Override
    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void start() {
        
    }

    @Override
    public void loop() {
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();
    }

}
