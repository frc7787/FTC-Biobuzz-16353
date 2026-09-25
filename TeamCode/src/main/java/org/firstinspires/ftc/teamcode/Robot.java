package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;

import org.firstinspires.ftc.teamcode.control.NextRobot;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Flywheel;
import org.firstinspires.ftc.teamcode.subsystems.Intake;

import java.util.Set;

import dev.nextftc.robot.Mechanism;

public class Robot implements NextRobot {
    public Flywheel flywheel = new Flywheel();
    public Intake intake = new Intake();
    public Drivetrain drivetrain = new Drivetrain();

    public Robot() {

    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(flywheel, intake);
    }
}
