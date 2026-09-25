package org.firstinspires.ftc.teamcode.subsystems;

import static com.pedropathing.ivy.commands.Commands.infinite;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.control.Robot;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Intake implements Mechanism {

    private NextMotor motor = new NextMotor("intakeMotor");
    private double INTAKE_POWER = 1.0;
    private double OUTTAKE_POWER = 1.0;
    private double intake_power = 1.0;

    private boolean activated = false;

    private boolean auto = false;

//    public Intake(Robot robot) {
//        motor = robot.hardwareMap.get(DcMotorEx.class, "intakeMotor");
//
//        telemetry = robot.telemetry;
//    }

    public Command intake() {
        return run(INTAKE_POWER);
    }
    public Command outtake() {
        return run(OUTTAKE_POWER);
    }

//    public Command run(Double power) {
//        return Command.build()
//                .setStart(() -> motor.setThrottle(power))
//                .setEnd(endCondition -> motor.setThrottle(0.0))
//                .requiring(this);
//    }
    public Command run(Double power) {
        return instant(() -> motor.setThrottle(power))
                .setEnd(endCondition -> motor.setThrottle(0.0))
                .requiring(motor);
    }

    public void setIntakePower(float power) {
        intake_power = power;
    }

    @Override
    public void periodic() {
            if (!auto) motor.setThrottle(intake_power);
    }


}
