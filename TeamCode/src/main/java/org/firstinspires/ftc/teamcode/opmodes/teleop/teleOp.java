package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.control.Robot;

import java.util.List;

import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.OpModeHook;

@NextTeleop(name = "TeleOp")
public class teleOp extends NextOpMode {
    public final Robot robot;

    public teleOp(Robot robot) {
        super((NextRobot) robot);
        this.robot = robot;
    }


    @Override
    public void start() {
        robot.flywheel.on();
        robot.drivetrain.startDrive(gamepad1);
    }

    @Override
    public void periodic() {
        robot.periodic();

        if (gamepad2.dpadDownWasPressed()) {
            robot.flywheel.adjustTargetVelocity(-100.0);
        }
        if (gamepad2.dpadUpWasPressed()) {
            robot.flywheel.adjustTargetVelocity(100.0);
        }
        if (gamepad2.dpadLeftWasPressed()) {
            robot.flywheel.adjustTargetVelocity(20.0);
        }
        if (gamepad2.dpadRightWasPressed()) {
            robot.flywheel.adjustTargetVelocity(-20.0);
        }
        if (gamepad2.squareWasReleased()) {
            robot.flywheel.isActive = !robot.flywheel.isActive;
        }
        if (gamepad2.right_trigger > 0.05) {
            robot.intake.setIntakePower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > 0.05) {
            robot.intake.setIntakePower(-gamepad2.left_trigger);
        } else {
            robot.intake.setIntakePower((float) 0.0);
        }

        robot.follower.manual(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x);

        robot.follower.update();
        Scheduler.execute();
    }
}
