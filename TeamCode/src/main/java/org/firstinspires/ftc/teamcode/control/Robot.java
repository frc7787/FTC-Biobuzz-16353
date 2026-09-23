package org.firstinspires.ftc.teamcode.control;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.PewPew;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class Robot {
    public final HardwareMap hardwareMap;
    public final Telemetry telemetry;
    public final PewPew flywheel;
    public final Intake intake;
    public Follower follower;

    public Robot(OpMode opMode) {
        hardwareMap = opMode.hardwareMap;
        follower = Constants.create(hardwareMap);
        telemetry = opMode.telemetry;
        flywheel = new PewPew(this);
        intake = new Intake(this);
        periodic();
    }
    public void periodic() {
        flywheel.periodic();
    }
}
