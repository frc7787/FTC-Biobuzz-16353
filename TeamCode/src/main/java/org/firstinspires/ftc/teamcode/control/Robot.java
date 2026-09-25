package org.firstinspires.ftc.teamcode.control;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.PewPew;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Set;

import dev.nextftc.robot.Mechanism;

public class Robot implements NextRobot {
    public Flywheel flywheel = new Flywheel();
    public Intake intake = new Intake();
    public Drivetrain drivetrain = new Drivetrain();
    public Follower follower;


    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(flywheel, intake);
    }
}
