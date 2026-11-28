package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name= "rookieTeleOp", group = "$")
public class rookieTeleOp extends OpMode {
    private DcMotorEx  frontRight,frontLeft, backRight, backLeft,
            intakeMotor;
    private int offHome;
    @Override
    public void init() {

        frontRight = hardwareMap.get(DcMotorEx.class,"frontRight");
        backRight = hardwareMap.get(DcMotorEx.class,"backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class,"frontLeft");
        backLeft = hardwareMap.get(DcMotorEx.class,"backLeft");
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        intakeMotor = hardwareMap.get(DcMotorEx.class,"intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void loop() {
/*        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;
*/      double drive = -gamepad1.left_stick_y * 0.70;
        double strafe = gamepad1.left_stick_x * 0.35;
        double rotate = gamepad1.right_stick_x * 0.35;
        double denominator = Math.max(
                Math.abs(drive) + Math.abs(strafe) + Math.abs(rotate), 1
        );

        double frontLeftPower = (drive + strafe + rotate) / denominator;
        double backLeftPower = (drive - strafe + rotate) / denominator;
        double frontRightPower = (drive - strafe - rotate) / denominator;
        double backRightPower = (drive + strafe - rotate) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
        offHome = intakeMotor.getCurrentPosition()-(intakeMotor.getCurrentPosition()%393);
        if(Math.abs(gamepad2.right_trigger)>0.1) {
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            intakeMotor.setPower(gamepad2.right_trigger * gamepad2.right_trigger);
        }else {
            intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            intakeMotor.setPower(-gamepad2.left_trigger * gamepad2.left_trigger);
        }if(gamepad2.xWasPressed()){
            intakeMotor.setTargetPosition(intakeMotor.getCurrentPosition());
            intakeMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeMotor.setPower(0.8);

        }
        telemetry.addData("Current position",intakeMotor.getCurrentPosition());
    }
}
