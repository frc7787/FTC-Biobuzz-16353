package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.control.Math;
import org.firstinspires.ftc.teamcode.control.Robot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.units.measuretypes.AngularVelocity;

public class PewPew {
    private NextMotor Motor;
    private final Telemetry telemetry;
    private Double targetVelocity = 3500.0;
    public boolean isActive = false;

    public PewPew(Robot robot) { //class constructor
        Motor = robot.hardwareMap.get(
                NextMotor.class,
                "pewPewMotor"
        );
        telemetry = robot.telemetry;
    }
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
        return Motor.getEncoderVelocity();
    }

    public Command periodic() {
        return infinite(() -> {
            if (isActive) {
                Motor.setVelocitySetpoint(RotationsPerMinute.of(targetVelocity));
            } else {
                Motor.setVelocitySetpoint(RotationsPerMinute.of(0));
            }
            telemetry.addData("shooter Velocity:", this.getVelocity());
        }).requiring(this);
    }
}
