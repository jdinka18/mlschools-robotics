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

@Autonomous(name = "Simple Color Senor Mode", group = "Auto Modes")
// @Disabled
public class ColorSensor_Auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareSkyBot robot = new HardwareSkyBot();   // Use a SkyBot's hardware
    //private ElapsedTime runtime = new ElapsedTime();


    // power variables for motors (from SkyBot Class)
    public static final double POWER_FULL = 1.0;
    public static final double POWER_HALF = 0.5;
    public static final double POWER_STOP = 0.0;
    public static final double POWER_LIFT = 0.6;


    // servo positions (from SkyBot Class)
    public final static double LEFTBLOCK_READY = 0.9;
    public final static double RIGHTBLOCK_READY = 0.1;

    public final static double LEFTBLOCK_GRAB = 1;
    public final static double RIGHTBLOCK_GRAB = 0;

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);


        // initialize the array to display the color values
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        if (robot.colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)robot.colorSensor).enableLight(true);
        }

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.colorArm.setPosition(0.1);
        sleep(1000);

        robot.leftDrive.setPower(0.3);
        robot.rightDrive.setPower(0.3);
        sleep(200);

        robot.leftDrive.setPower(0.0);
        robot.rightDrive.setPower(0.0);
        sleep(200);

        // Color Math
        NormalizedRGBA colors = robot.colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        int color = colors.toColor();
        float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
        colors.red   /= max;
        colors.green /= max;
        colors.blue  /= max;
        color = colors.toColor();

        // Detect color, move as correctly
        if(Color.red(color)>0x90) {
            telemetry.addLine("Red");
            robot.leftDrive.setPower(0.2);
            sleep(200);
            robot.leftDrive.setPower(0.0);
            sleep(100);

        }
        else if(Color.blue(color)>0x50) {
            telemetry.addLine("Blue");
            robot.rightDrive.setPower(0.2);
            sleep(200);
            robot.rightDrive.setPower(0.0);
            sleep(100);

        }
        else {
            telemetry.addLine("Nothing");

        }

        robot.colorArm.setPosition(0.5);
        sleep(1000);

        // Last Step
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);

        while(opModeIsActive()) {

            // DISPLAY COLOR on phone for entire OpMode

            Color.colorToHSV(colors.toColor(), hsvValues);
            telemetry.addLine()
                    .addData("H", "%.3f", hsvValues[0])
                    .addData("S", "%.3f", hsvValues[1])
                    .addData("V", "%.3f", hsvValues[2]);
            telemetry.addLine()
                    .addData("a", "%.3f", colors.alpha)
                    .addData("r", "%.3f", colors.red)
                    .addData("g", "%.3f", colors.green)
                    .addData("b", "%.3f", colors.blue);

            /** We also display a conversion of the colors to an equivalent Android color integer.
             * @see Color */

            telemetry.addLine("raw Android color: ")
                    .addData("a", "%02x", Color.alpha(color))
                    .addData("r", "%02x", Color.red(color))
                    .addData("g", "%02x", Color.green(color))
                    .addData("b", "%02x", Color.blue(color));

            colors.red   /= max;
            colors.green /= max;
            colors.blue  /= max;
            color = colors.toColor();

            telemetry.addLine("normalized color:  ")
                    .addData("a", "%02x", Color.alpha(color))
                    .addData("r", "%02x", Color.red(color))
                    .addData("g", "%02x", Color.green(color))
                    .addData("b", "%02x", Color.blue(color));

            if(Color.red(color)>0x90) {
                telemetry.addLine("Red");
                robot.leftDrive.setPower(0.2);
                sleep(200);
                robot.leftDrive.setPower(0.0);
                sleep(100);

            }
            else if(Color.blue(color)>0x50) {
                telemetry.addLine("Blue");
                robot.rightDrive.setPower(0.2);
                sleep(200);
                robot.rightDrive.setPower(0.0);
                sleep(100);

            }
            else {
                telemetry.addLine("Nothing");
            }

            telemetry.update();

        }

        }

    }

// author's note - Below code is alternate solution, but is a little more messy
        /*
        // Step through each leg of the path, ensuring that the Auto mode has not been stopped along the way

        // Step 1:  Drive forward for 3 seconds
        robot.leftDrive.setPower(FORWARD_SPEED);
        robot.rightDrive.setPower(FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 3.0)) {
            telemetry.addData("Path", "Leg 1: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 2:  Spin right for 1.3 seconds
        robot.leftDrive.setPower(TURN_SPEED);
        robot.rightDrive.setPower(-TURN_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.3)) {
            telemetry.addData("Path", "Leg 2: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 3:  Drive Backwards for 1 Second
        robot.leftDrive.setPower(-FORWARD_SPEED);
        robot.rightDrive.setPower(-FORWARD_SPEED);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < 1.0)) {
            telemetry.addData("Path", "Leg 3: %2.5f S Elapsed", runtime.seconds());
            telemetry.update();
        }

        // Step 4:  Stop and close the claw.
        robot.leftDrive.setPower(0);
        robot.rightDrive.setPower(0);
        robot.leftClaw.setPosition(1.0);
        robot.rightClaw.setPosition(0.0);

        */
