package org.firstinspires.ftc.teamcode.opmodes.teleop;
import com.qualcomm.robotcore.hardware.Servo;
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
    private Servo indicator;

    @Override public void init() {
        follower = Constants.create(hardwareMap);

        flywheelMotorOne = hardwareMap.get(DcMotorEx.class, "flywheelMotorOne");
        flywheelMotorOne.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheelMotorTwo = hardwareMap.get(DcMotorEx.class, "flywheelMotorTwo");
        flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        indicator = hardwareMap.get(Servo.class, "indicator");
    }




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
    private double THREEPOLL = 2460;
    private double FOURPOLL = 2660;
    //RPM FOR NECTAR
    private double ONENEC = 2400;
    private double TWONEC = 2560;
    private double THREENEC = 2660;
    private double FOURNEC = 2660;
    private double currentRpm = ONEPOLL;
    private int amountAway = POLL_HOME;
    private boolean REACHED_POS = false;
    private boolean shooterOn = false;
    private boolean REVERSE = false;
    private boolean FORWARD = false;
    private int sign_switch = 1;
    enum Poll_shooting{MANUAL,MOVE_HOME,CHECK_HOME,IDLE,POLL_POS,CHECK_POS,AT_POS,POLL_LAUNCH};
    private Poll_shooting state = Poll_shooting.IDLE;
    private boolean NEGATIVE_MODE = false;
    private static final double RED = 0.38;
    private static final double BLUE = 0.50;

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

        if (Math.abs(gamepad2.right_trigger) > 0.1) {
            intakeMotor.setPower(gamepad2.right_trigger);
        } else {
            intakeMotor.setPower(-gamepad2.left_trigger);
        }


        if (gamepad2.options) {
            flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }
        if (gamepad1.crossWasPressed()) {
            NEGATIVE_MODE = true;
        }
        if (gamepad1.triangleWasPressed()) {
            NEGATIVE_MODE = false;
        }
        if (NEGATIVE_MODE) {
            sign_switch = -1;
            indicator.setPosition(RED);
        }
        if (!NEGATIVE_MODE) {
            sign_switch = 1;
            indicator.setPosition(BLUE);
        }
        if (gamepad2.triangleWasPressed()) {
            currentPosition = POLL_ONE;
            currentRpm = ONEPOLL;
            state = Poll_shooting.POLL_POS;

        }

        if (gamepad2.circleWasPressed()) {
            currentPosition = POLL_TWO;
            currentRpm = TWOPOLL;
            state = Poll_shooting.POLL_POS;
        }

        if (gamepad2.crossWasPressed()) {
            currentPosition = POLL_THREE;
            currentRpm = THREEPOLL;
            state = Poll_shooting.POLL_POS;
        }

        if (gamepad2.squareWasPressed()) {
            currentPosition = POLL_FOUR;
            currentRpm = FOURPOLL;
            state = Poll_shooting.POLL_POS;
        }if(gamepad2.leftBumperWasPressed()){
            state = Poll_shooting.MOVE_HOME;
        }if (gamepad2.rightBumperWasPressed() && state == Poll_shooting.AT_POS){
            timer.reset();
            state = Poll_shooting.POLL_LAUNCH;
        }
        switch(state) {
            case MOVE_HOME:

                flywheelMotorOne.setTargetPosition(amountAway);
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                flywheelMotorOne.setPower(0.5);
                state = Poll_shooting.CHECK_HOME;
                break;
            case CHECK_HOME:

                if ((Math.abs(flywheelMotorOne.getCurrentPosition() - amountAway*sign_switch)<5)) {
                    flywheelMotorOne.setPower(0);
                    flywheelMotorTwo.setPower(0);
                    state = Poll_shooting.IDLE;
                }
                break;
          case IDLE:
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if(gamepad1.dpad_up){
                    flywheelMotorOne.setPower(0.03);
                    flywheelMotorTwo.setPower(0.03);
                    indicator.setPosition(RED);
                } else if (gamepad1.dpad_down) {
                    flywheelMotorTwo.setPower(-0.03);
                    flywheelMotorOne.setPower(-0.03);
                    indicator.setPosition(BLUE);
                }else{
                    flywheelMotorOne.setPower(0);
                    flywheelMotorTwo.setPower(0);
                }if(gamepad1.dpadRightWasPressed() || gamepad1.dpadLeftWasPressed()){
                    flywheelMotorOne.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    flywheelMotorTwo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }
              break;

            case POLL_POS:
                flywheelMotorOne.setTargetPosition(currentPosition*sign_switch);
                flywheelMotorOne.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                flywheelMotorOne.setPower(0.7);

                state = Poll_shooting.CHECK_POS;

                break;
            case CHECK_POS:
                if (Math.abs(flywheelMotorOne.getCurrentPosition() - currentPosition*sign_switch)< 5) {
                    flywheelMotorTwo.setPower(0);
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
                flywheelMotorOne.setVelocity(currentRpm*sign_switch);
                flywheelMotorTwo.setVelocity(currentRpm*sign_switch);

                if (timer.seconds() > 1.6) {
                    flywheelMotorOne.setPower(0);
                    flywheelMotorTwo.setPower(0);

                    flywheelMotorOne.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    flywheelMotorTwo.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    state = Poll_shooting.MOVE_HOME;
                }
                break;


        }






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
        telemetry.addData("TIMER",timer);
        telemetry.addData("state",state);
        telemetry.addData("dpadUp",gamepad1.dpad_up);
        telemetry.addData("dpadDown",gamepad1.dpad_down);
        telemetry.update();


    }
}
