package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.control.Robot;

import dev.nextftc.hardware.actuators.NextMotor;

public class Intake {

    private DcMotorEx motor;
    private Telemetry telemetry;
    private double INTAKE_POWER = 1.0;
    private double OUTTAKE_POWER = 1.0;

    private boolean activated = false;

    public Intake(Robot robot) {
        motor = robot.hardwareMap.get(DcMotorEx.class, "intakeMotor");

        telemetry = robot.telemetry;
    }

    public Command intake() {
        return run(INTAKE_POWER);
    }

    public Command outtake() {
        return run(OUTTAKE_POWER);
    }

    public Command run(Double power) {
        return Command.build()
                .setStart(() -> motor.setPower(power))
                .setEnd(endCondition -> motor.setPower(0.0))
                .requiring(this);
    }

}
