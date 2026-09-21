package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.control.Robot;

public class teleOp extends OpMode {
    protected Robot robot;

    @Override
    public void init() {
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
            robot.flywheel.adjustTargetVelocity(50.0);
        }
        if (gamepad2.dpadRightWasPressed()) {
            robot.flywheel.adjustTargetVelocity(-50.0);
        }

    }
}
