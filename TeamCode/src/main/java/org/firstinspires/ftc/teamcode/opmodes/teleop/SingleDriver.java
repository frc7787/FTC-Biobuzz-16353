package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(group = "$")
public class SingleDriver extends OpMode {
    private DcMotorEx flywheelMotorOne,
            flywheelMotorTwo,
            intakeMotor;
    private Follower follower;

    @Override public void init() {
        follower = Constants.create(hardwareMap);

        flywheelMotorOne = hardwareMap.get(DcMotorEx.class, "flywheelMotorOne");
        flywheelMotorOne.setDirection(DcMotorSimple.Direction.REVERSE);

        flywheelMotorTwo = hardwareMap.get(DcMotorEx.class, "flywheelMotorTwo");

        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

    }

    private static final double NEAR_RPM = 1200;
    private static final double FAR_RPM  = 1800;

    private double currentRpm = FAR_RPM;

    private boolean shooterOn = false;

    enum TransferState {
        HOMING,
        UP,
        DOWN
    }

    @Override public void loop() {
        follower.manual(
                ManualDrive.fieldCentric(
                        -gamepad1.left_stick_y,
                        -gamepad1.left_stick_x,
                        -gamepad1.right_stick_x,
                        follower.pose().heading()
                )
        );
        follower.update();

        if (gamepad1.options) {
            follower.localizer.setHeading(0.0);
        }

        if (gamepad1.dpad_down) {
            shooterOn = false;
        }
        if (gamepad1.dpad_up) {
            shooterOn = true;
        }

        if (gamepad1.triangle) {
            currentRpm = NEAR_RPM;
        } else if (gamepad2.cross) {
            currentRpm = FAR_RPM;
        }

        if (shooterOn) {
            flywheelMotorOne.setVelocity(currentRpm, AngleUnit.DEGREES);
            flywheelMotorTwo.setVelocity(currentRpm, AngleUnit.DEGREES);
        } else {
            flywheelMotorOne.setPower(0.0);
            flywheelMotorTwo.setPower(0.0);
        }

        double power = gamepad1.left_trigger - gamepad1.right_trigger;

        intakeMotor.setPower(power);

        displayDebugInfo();
    }

    private void displayDebugInfo() {
        telemetry.addLine("Target RPM: " + currentRpm);
        telemetry.addLine("Actual RPM: " + flywheelMotorOne.getVelocity());

        if (shooterOn) {
            if (currentRpm == NEAR_RPM) {
                telemetry.addLine("Near Mode");
            } else if (currentRpm == FAR_RPM) {
                telemetry.addLine("Far Mode");
            }
        } else {
            telemetry.addLine("Shooter Off");
        }

    }
}