package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.Set;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class Robot implements NextRobot {
    public Flywheel flywheel;
    public Intake intake;
    public Drivetrain drivetrain;

    public Robot() {
        flywheel = new Flywheel();
        intake = new Intake();
        drivetrain = new Drivetrain();
    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(flywheel, intake);
    }
}
