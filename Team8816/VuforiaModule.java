package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Lane Doak of Recharged Orange on 9/9/17.
 */
@Autonomous(name = "VuforiaRelic", group = "Vuforia")
@Disabled
public class VuforiaModule extends LinearOpMode {

    // Vuforia Stuff

    public static final String TAG = "Vuforia Navigation Sample";
    OpenGLMatrix lastLocation = null;
    VuforiaLocalizer vuforia;

    // this is to tell the robot to bring the block to left, center, or right
    // isLCR = null;
    int isLCR;

    // color sensor math

    /* Declare OpMode members. */
    HardwareSkyBot robot = new HardwareSkyBot();   // Use a SkyBot's hardware

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};
    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    final double SCALE_FACTOR = 255;

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // Vuforia Stuff

        robot.init(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "ARMl4sr/////AAAAGW7XCTx7E0rTsT4i0g6I9E8IY/EGEWdA5QHmgcnvsPFeuf+2cafgFWlJht6/m4ps4hdqUeDgqSaHurLTDfSET8oOvZUEOiMYDq2xVxNDQzW4Puz+Tl8pOFb1EfCrP28aBkcBkDfXDADiws03Ap/mD///h0HK5rVbe3KYhnefc0odh1F7ZZ1oxJy+A1w2Zb8JCXM/SWzAVvB1KEAnz87XRNeaJAon4c0gi9nLAdZlG0jnC6bx+m0140C76l14CTthmzSIdZMBkIb8/03aQIouFzLzz+K1fvXauT72TlDAbumhEak/s5pkN6L555F28Jf8KauwCnGyLnePxTm9/NKBQ4xW/bzWNpEdfY4CrBxFoSkq";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; // Use FRONT Camera (Change to BACK if you want to use that one)


        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // debugging purposes

        // color math for detecting the jewels
/*
        // initialize the array to display the color values
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        if (robot.colorSensorNormalized instanceof SwitchableLight) {
            ((SwitchableLight) robot.colorSensorNormalized).enableLight(true);
        }
        */

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        relicTrackables.activate(); // Activate Vuforia

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            // Test to see if image is visable and set isLCR to 1,2,3 (left, right, center)

            if (vuMark == RelicRecoveryVuMark.LEFT) { // Test to see if Image is the "LEFT" image and display value.
                telemetry.addData("VuMark is", "I SEE Left");
                isLCR = 1;
                telemetry.update();
            } else if (vuMark == RelicRecoveryVuMark.RIGHT) { // Test to see if Image is the "RIGHT" image and display values.
                telemetry.addData("VuMark is", "I SEE Right");
                isLCR = 2;
                telemetry.update();
            } else if (vuMark == RelicRecoveryVuMark.CENTER) { // Test to see if Image is the "CENTER" image and display values.
                telemetry.addData("VuMark is", "I SEE Center");
                isLCR = 3;
                telemetry.update();
            } else if (vuMark == RelicRecoveryVuMark.UNKNOWN) {

                telemetry.addData("VuMark is", "I SEE Nothing");
                isLCR = 3;
                telemetry.update();

            }

            sleep(1500);

            telemetry.addData("isLCR", isLCR);
            telemetry.update();

            relicTrackables.deactivate();

            telemetry.addData("Using the Vuforia...", isLCR);
            telemetry.addLine("We got to the switch case");
            telemetry.update();


            switch (isLCR) {

                case 1:
                    robot.driveStraight(0.25);
                    sleep(1000);
                    robot.turnLeft(0.3);
                    sleep(1000);
                    telemetry.addData("Vuforia 1", "turning to left...");
                    break;

                case 2:
                    robot.driveStraight(0.25);
                    sleep(1000);
                    robot.turnLeft(0.3);
                    sleep(1500);
                    telemetry.addData("Vuforia 2", "turning to right...");
                    break;

                case 3:
                    robot.driveStraight(0.25);
                    sleep(1000);
                    robot.turnLeft(0.3);
                    sleep(1200);
                    telemetry.addData("Vuforia 3", "turning to center");
                    break;

                default:
                    robot.driveStraight(0.25);
                    sleep(1000);
                    robot.turnLeft(0.3);
                    sleep(1200);
                    telemetry.addData("Vuforia 3", "turning to center...");
                    break;

            }

            robot.stopMotors();
            sleep(500);

            // Last Step - Complete!
            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(500);

    }

}
