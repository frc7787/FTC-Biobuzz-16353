package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp(name="novScrimmageTele", group = "$")
public class novScrimmageTele extends OpMode {
    private DcMotorEx flywheelMotorOne,
            flywheelMotorTwo,
            intakeMotor;
    private Follower follower;

    @Override public void init() {
        follower = Constants.create(hardwareMap);

        flywheelMotorOne = hardwareMap.get(DcMotorEx.class, "flywheelMotorOne");
        flywheelMotorOne.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheelMotorTwo = hardwareMap.get(DcMotorEx.class, "flywheelMotorTwo");
        flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

    }



    private int amountAway;
    ElapsedTime timer = new ElapsedTime();
    //POSITION FOR FLIPPER
    private int POLL_HOME = 0;
    private int POLL_ONE = 70;
    private int POLL_TWO = 67;
    private int POLL_THREE = 60;
    private int POLL_FOUR = 0;
    private int currentPosition = POLL_HOME;
    private boolean HOMING_POS = false;
    //RPM FOR POLLEN
    // NOTE THAT CENTER OF MASS OF BALLS SHOULD BE CENTERED EXCEPT POLLEN 4 or NECTAR 4 HAS TO DO A COMPLETE ROTATION
    private double HOMEPOLL = 0;
    private double ONEPOLL = 2200;
    private double TWOPOLL = 2200;
    private double THREEPOLL = 2340;
    private double FOURPOLL = 2660;
    //RPM FOR NECTAR
    private double ONENEC = 2400;
    private double TWONEC = 2560;
    private double THREENEC = 2660;
    private double FOURNEC = 2660;
    private double currentRpm = ONEPOLL;
    private boolean REACHED_POS = false;
    private boolean shooterOn = false;
    enum Poll_shooting{MOVE_HOME,CHECK_HOME,IDLE,POLL_POS,AT_POS,POLL_LAUNCH};
    private Poll_shooting state = Poll_shooting.IDLE;

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


        if (gamepad2.options) {
            flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }
        double motorPower = gamepad2.right_trigger;
        flywheelMotorTwo.setPower(gamepad2.right_trigger);
        flywheelMotorOne.setPower(motorPower*motorPower);


        if (gamepad2.triangle){
            currentPosition = POLL_ONE;
            currentRpm = ONEPOLL;
            state = Poll_shooting.POLL_POS;
        }
        if(gamepad2.circle){
            currentPosition = POLL_TWO;
            currentRpm = TWOPOLL;
            state = Poll_shooting.POLL_POS;
        }
        if(gamepad2.cross){
            currentPosition = POLL_THREE;
            currentRpm = THREEPOLL;
            state = Poll_shooting.POLL_POS;
        }
        if(gamepad2.square){
            currentPosition = POLL_FOUR;
            currentRpm = FOURPOLL;
            state = Poll_shooting.POLL_POS;

        }if(gamepad2.left_bumper){
            state = Poll_shooting.MOVE_HOME;
        }if (gamepad2.right_bumper && state == Poll_shooting.AT_POS){
            state = Poll_shooting.POLL_LAUNCH;
        }
        switch(state) {
            case MOVE_HOME:
                amountAway = flywheelMotorOne.getCurrentPosition() - (flywheelMotorOne.getCurrentPosition() % 179);
                flywheelMotorOne.setTargetPosition(amountAway);
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                flywheelMotorOne.setPower(0.7);
                state = Poll_shooting.CHECK_HOME;
                break;
            case CHECK_HOME:
                if (Math.abs(flywheelMotorOne.getCurrentPosition() - amountAway) < 5) {
                    flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    state = Poll_shooting.IDLE;
                }else{
                    state = Poll_shooting.MOVE_HOME;
                }
                break;
            case IDLE:
                flywheelMotorOne.setPower(0);
                flywheelMotorTwo.setPower(0);
                break;

            case POLL_POS:
                flywheelMotorOne.setTargetPosition(currentPosition);
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                flywheelMotorOne.setPower(0.6);
                if (Math.abs(flywheelMotorOne.getCurrentPosition() - currentPosition)<2) {
                    flywheelMotorOne.setPower(0);
                    flywheelMotorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    state = Poll_shooting.AT_POS;
                }
                break;
            case AT_POS:
                flywheelMotorOne.setPower(0);
                break;
            case POLL_LAUNCH:
                    timer.reset();
                    flywheelMotorOne.setVelocity(currentRpm);
                    flywheelMotorTwo.setVelocity(currentRpm);
                    break;


        }




        double power = gamepad1.left_trigger - gamepad1.right_trigger;

        intakeMotor.setPower(power);

        displayDebugInfo();
    }






    private void displayDebugInfo() {
        telemetry.addLine("Target RPM: " + currentRpm);
        telemetry.addLine("Actual RPM: " + flywheelMotorOne.getVelocity());


            if (currentPosition == POLL_ONE)  {
                telemetry.addLine("ONEBALL MODE pollen");
            } else if (currentPosition == POLL_TWO) {
                telemetry.addLine("TWOBALl MODE pollen");
            } else if (currentRpm == POLL_THREE) {
                telemetry.addLine("THREEBALL MODE pollen");
            }else if (currentRpm == POLL_FOUR){
                telemetry.addLine("FOURBALL MODE pollen");
            }

        telemetry.addData("modulo 180", flywheelMotorOne.getCurrentPosition()%180);
        telemetry.addData("amount Away", amountAway);
        telemetry.addData("Motor Position:",flywheelMotorOne.getCurrentPosition());
        telemetry.addData("Motor Position 2: ",flywheelMotorTwo.getCurrentPosition());
        telemetry.addData("current rpm",+ currentRpm);
        telemetry.addData("real rpm", flywheelMotorOne.getVelocity());

        telemetry.update();

    }
}
