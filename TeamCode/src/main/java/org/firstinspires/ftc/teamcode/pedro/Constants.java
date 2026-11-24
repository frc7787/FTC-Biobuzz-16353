package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeftMotor");
        c.frontRightName.set("frontRightMotor");
        c.backLeftName.set("backLeftMotor");
        c.backRightName.set("backRightMotor");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.manualBrakeMode.set(true);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(5.863010076087291);
        //0.3984182838379868
        //1.0784867053895486
        //5.863010076087291
        c.yPodOffset.set(1.0784867053895486);
        //-1.0449877310925582
        //-0.2915391584080974
        //1.0784867053895486
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.26022702610695725);
                Controller secondaryTranslationalForward = Controller.proportional(0.09614693275232063);
                Controller primaryTranslationalLateral = Controller.proportional(0.35021306435548893);
                Controller secondaryTranslationalLateral = Controller.proportional(0.12939436941392735);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.011824463761117314));
                c.brake.set(Controller.proportionalFeedforward(0.010050794196949717));

                c.headingFeedback.set(Controller.proportional(6.167476818616893));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0563187212713794, 0.004569195976980196));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10721907953935796, 0.07067029265935976));
                c.quadraticBrakeCoefficients.set(Matrix.diag(9.86772004874827E-4, 0.0013035807338454962));

                c.maxAchievableForwardVelocity.set(83.53914781762354);
                c.maxAchievableStrafeVelocity.set(67.89227022794799);
                c.naturalForwardDeceleration.set(25.751452521264454);
                c.naturalStrafeDeceleration.set(60.80567597175896);
            }
    ); // end of Karim & Josh Foresight

    /*
    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3043919546040935);
                Controller secondaryTranslationalForward = Controller.proportional(0.11246469372338866);
                Controller primaryTranslationalLateral = Controller.proportional(0.5021977122054305);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1855486356898958);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01263276888414395));
                c.brake.set(Controller.proportionalFeedforward(0.010737853551522358));

                c.headingFeedback.set(Controller.proportional(5.681452392148098));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05699043863122662, 0.0033630888834394177));

                c.linearBrakeCoefficients.set(Matrix.diag(0.11696975726286628, 0.05986430016083318));
                c.quadraticBrakeCoefficients.set(Matrix.diag(5.362605567024223E-4, 0.001447066989189596));

                c.maxAchievableForwardVelocity.set(80.89903903411074);
                c.maxAchievableStrafeVelocity.set(65.45752340827845);
                c.naturalForwardDeceleration.set(29.95762958675175);
                c.naturalStrafeDeceleration.set(61.64229827973857);
            }
    ); // end of Tyler's foresight

     */

}