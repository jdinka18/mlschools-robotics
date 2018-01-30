/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "BlueVu - Color Senor", group = "Vuforia")
// @Disabled
public class Blue_ColorSensor_Vuforia extends LinearOpMode {

    // Vuforia Stuff

    public static final String TAG = "Vuforia Navigation Sample";
    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.

    VuforiaLocalizer vuforia;
    OpenGLMatrix lastLocation = null;
    int isLCR = 0;

    // color sensor math

    /* Declare OpMode members. */
    HardwareSkyBot robot = new HardwareSkyBot();   // Use a SkyBot's hardware
    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};
    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // note - all telemetry are for debugging purposes
        robot.init(hardwareMap);

        // Vuforia Stuff

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "ARMl4sr/////AAAAGW7XCTx7E0rTsT4i0g6I9E8IY/EGEWdA5QHmgcnvsPFeuf+2cafgFWlJht6/m4ps4hdqUeDgqSaHurLTDfSET8oOvZUEOiMYDq2xVxNDQzW4Puz+Tl8pOFb1EfCrP28aBkcBkDfXDADiws03Ap/mD///h0HK5rVbe3KYhnefc0odh1F7ZZ1oxJy+A1w2Zb8JCXM/SWzAVvB1KEAnz87XRNeaJAon4c0gi9nLAdZlG0jnC6bx+m0140C76l14CTthmzSIdZMBkIb8/03aQIouFzLzz+K1fvXauT72TlDAbumhEak/s5pkN6L555F28Jf8KauwCnGyLnePxTm9/NKBQ4xW/bzWNpEdfY4CrBxFoSkq";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK; // Use FRONT Camera (Change to BACK if you want to use that one)
        parameters.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES; // Display Axes

        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // debugging purposes

        // color math for detecting the jewels

        // initialize the array to display the color values
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        if (robot.colorSensorNormalized instanceof SwitchableLight) {
            ((SwitchableLight) robot.colorSensorNormalized).enableLight(true);
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        relicTrackables.activate(); // Activate Vuforia

        while(opModeIsActive()) {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

            // Test to see if image is visable

                if (vuMark == RelicRecoveryVuMark.LEFT) { // Test to see if Image is the "LEFT" image and display value.
                    telemetry.addData("VuMark is", "Left");
                    isLCR = 1;
                } else if (vuMark == RelicRecoveryVuMark.RIGHT) { // Test to see if Image is the "RIGHT" image and display values.
                    telemetry.addData("VuMark is", "Right");
                    isLCR = 2;
                } else if (vuMark == RelicRecoveryVuMark.CENTER) { // Test to see if Image is the "CENTER" image and display values.
                    telemetry.addData("VuMark is", "Center");
                    isLCR = 3;
                } else {

                    telemetry.addData("VuMark is", "Unknown");
                    isLCR = 0;

                }
             /*else { // no else statement, essentially wait until recognize the left, right, center
                telemetry.addData("VuMark", "not visible");
                isLCR = 0;
            }*/
            telemetry.addData("isLCR", isLCR);
            telemetry.update();
            relicTrackables.deactivate();


            // get the color sensor in position
            robot.colorArm.setPosition(0.93);
            sleep(1500);

            // drive backwards a little so that the color sensor is close to the jewels
            robot.driveStraight(0.3);
            sleep(200);

            robot.stopMotors();
            sleep(400);

            telemetry.addData("Servo", "Moved");
            telemetry.update();


            // Color Math
            NormalizedRGBA colors = robot.colorSensorNormalized.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);
            int color = colors.toColor();
            float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
            colors.red /= max;
            colors.green /= max;
            colors.blue /= max;
            color = colors.toColor();

            // Detect color, move as correctly

            // NOTE: The color sensor reads toward the left side

            // if red on left side
            if (Color.red(color) > 0x90) {
                telemetry.addLine("Red");

                // knock off the jewel
                robot.turnLeft(0.3);
                sleep(350);
                robot.stopMotors();
                sleep(500);

                robot.driveStraight(-0.2);
                sleep(500);

                robot.stopMotors();
                sleep(200);

                // stow the color arm
                robot.colorArm.setPosition(0.25);
                sleep(1000);

            }

            // if blue on left side
            else if (Color.blue(color) > 0x50) {
                telemetry.addLine("Blue");

                // knock off the jewel
                robot.turnRight(0.3);
                sleep(350);
                robot.stopMotors();
                sleep(500);

                robot.driveStraight(-0.2);
                sleep(500);

                // stow the color arm
                robot.colorArm.setPosition(0.25);
                sleep(1000);

                robot.turnLeft(-0.3);
                sleep(600);

                robot.stopMotors();
                sleep(500);

            }

            // cannot see the ball's color - do nothing
            else {
                // stow the color arm
                robot.colorArm.setPosition(0.25);
                sleep(1000);
                telemetry.addLine("Nothing");
                sleep(600);
            }

            telemetry.update();

            // Park the robot
            robot.stopMotors();
            sleep(200);

            // Get the top grippers ready for block
            robot.leftTop.setPosition(robot.LEFTTOP_READY);
            robot.rightTop.setPosition(robot.RIGHTTOP_READY);
            sleep(1500);

            // get the arms ready
            robot.armMotor.setPower(robot.POWER_HALF);
            sleep(1000);

            robot.armMotor.setPower(robot.POWER_STOP);
            sleep(200);

            if (robot.colorSensorNormalized instanceof SwitchableLight) {
                ((SwitchableLight) robot.colorSensorNormalized).enableLight(false);
                sleep(500);
            }


            // after this step
            // Last Step - Complete!
            telemetry.addData("Vuforia", "Using the math...");
            telemetry.update();
            sleep(1000);

            switch (isLCR) {

                case 0:
                    robot.turnLeft(0.5);
                    sleep(600);
                    robot.driveStraight(0.5);
                    sleep(500);
                    telemetry.addData("Vuforia", "turning left...");

                    break;

                case 1:
                    robot.turnLeft(0.5);
                    sleep(800);
                    robot.driveStraight(0.5);
                    sleep(500);
                    telemetry.addData("Vuforia", "turning left...");

                    break;

                case 2:
                    robot.turnLeft(0.5);
                    sleep(700);
                    robot.driveStraight(0.5);
                    sleep(500);
                    telemetry.addData("Vuforia", "turning left...");

                    break;

                case 3:
                    robot.turnLeft(0.5);
                    sleep(600);
                    robot.driveStraight(0.5);
                    sleep(500);
                    telemetry.addData("Vuforia", "turning left...");

                    break;

            }

            // Last Step - Complete!
            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);

        }

    }

}
