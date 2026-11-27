package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name= "rookieTeleOp", group = "$")
public class rookieTeleOp extends OpMode {
    private DcMotorEx  frontRight,frontLeft, backRight, backLeft,
            intakeMotor;
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
    }

    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x;
        double rotate = gamepad1.right_stick_x;


        double denominator = Math.max(Math.abs(drive) + Math.abs(strafe) + Math.abs(rotate), 1);
        double frontLeftPower = (drive + strafe + rotate);
        double backLeftPower = (drive - strafe + rotate);
        double frontRightPower = (drive - strafe - rotate);
        double backRightPower = (drive + strafe - rotate);

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
        if(gamepad2.rightBumperWasPressed()){
            intakeMotor.setPower(1);
        }

        if(gamepad2.left_bumper){
            intakeMotor.setPower(-1);

        }if(gamepad2.xWasPressed()){
            intakeMotor.setPower(0);
        }
        if(gamepad1.y){
            frontLeft.setPower(0.5);
        }if(gamepad1.b){
            frontRight.setPower(0.5);
        }if(gamepad1.a){
            backRight.setPower(0.5);

        }if(gamepad1.x){
            backLeft.setPower(0.5);
        }
    }
}
