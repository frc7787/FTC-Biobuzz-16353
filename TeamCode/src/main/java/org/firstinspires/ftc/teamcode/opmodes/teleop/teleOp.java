package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.control.Robot;

public class teleOp extends OpMode {
    protected Robot robot;

    @Override
    public void init() {
        Scheduler.reset();
        robot = new Robot(this);
    }

    @Override
    public void start() {
        robot.flywheel.on();
    }

    @Override
    public void loop() {
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
        if (gamepad2.rightBumperWasReleased()) {
            schedule(robot.intake.intake());
        }
        if (gamepad2.leftBumperWasReleased()) {
            schedule(robot.intake.outtake());
        }

        robot.follower.manual(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x);

        robot.follower.update();
        Scheduler.execute();
    }
}
