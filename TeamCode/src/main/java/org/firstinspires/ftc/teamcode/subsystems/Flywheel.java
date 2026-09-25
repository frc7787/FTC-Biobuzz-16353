package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;

import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.control.Math;
import org.firstinspires.ftc.teamcode.control.Robot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.units.measuretypes.AngularVelocity;

public class Flywheel implements Mechanism {
    private NextMotor leftMotor = new NextMotor("leftFlywheelMotor");
    private NextMotor rightMotor = new NextMotor("rightFlywheelMotor");
    private Double targetVelocity = 3500.0;
    public boolean isActive = false;

//    public Flywheel(Robot robot) { //class constructor
//        leftMotor = new NextMotor("leftFlywheelMotor");
//        leftMotor.setDirection(NextMotor.Direction.REVERSE);
//
//        rightMotor = new NextMotor("rightFlywheelMotor");
//
//        telemetry = robot.telemetry;
//    }
    public void on() {
        isActive = true;
    }

    public void off() {
        isActive = false;
    }

    public void adjustTargetVelocity(Double change) {
        targetVelocity += change;
    }

    public void setTargetVelocity(Double newVelocity) {
        isActive = true;
        targetVelocity = Math.MaxOf(0.0, newVelocity);
    }

    public AngularVelocity getVelocity() {
        return rightMotor.getEncoderVelocity();
    }

    @Override
    public void periodic() {
        if (isActive) {
            rightMotor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
            leftMotor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
        } else {
            rightMotor.setVelocitySetpoint(RotationsPerMinute.of(0));
            leftMotor.setVelocitySetpoint(RotationsPerMinute.of(0));
        }
        Telemetry.log("Flywheel Velocity:", this.getVelocity());
    }
}















