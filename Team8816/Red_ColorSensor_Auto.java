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

@Autonomous(name = "Red - Color Senor", group = "Color Sensor")
// @Disabled
public class Red_ColorSensor_Auto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareSkyBot robot = new HardwareSkyBot();   // Use a SkyBot's hardware

    // color sensor math

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;


    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        // note - all telemetry are for debugging purposes
        robot.init(hardwareMap);


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

        // get the color sensor in position
        robot.colorArm.setPosition(0.91);
        sleep(1500);

        // drive straight a little so that the color sensor is close to the jewels
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
            robot.turnRight(0.5);
            sleep(200);
            robot.stopMotors();
            sleep(500);

            robot.driveStraight(-0.3);
            sleep(400);

            robot.stopMotors();
            sleep(200);

            // stow the color arm
            robot.colorArm.setPosition(0.25);
            sleep(1000);

            /* straighten the robot to go to the safety zone
            robot.turnRight(-0.5);
            sleep(400);

            robot.stopMotors();
            sleep(1000);
            */

        }

        // if blue on left side
        else if (Color.blue(color) > 0x50) {
            telemetry.addLine("Blue");

            // knock off the jewel
            robot.turnLeft(0.5);
            sleep(200);
            robot.stopMotors();
            sleep(500);

            robot.driveStraight(-0.3);
            sleep(400);

            // stow the color arm
            robot.colorArm.setPosition(0.25);
            sleep(1000);

            robot.turnRight(-0.4);
            sleep(500);

            robot.stopMotors();
            sleep(500);

            /* turn right just a little to go straight toward the safety zone
            robot.turnRight(0.4);
            sleep(400);

            robot.stopMotors();
            sleep(1000);
            */


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

        /*
        // Get the top grippers ready for block
        robot.leftTop.setPosition(robot.LEFTTOP_READY);
        robot.rightTop.setPosition(robot.RIGHTTOP_READY);
        sleep(1500);

        // get the arms ready
        robot.armMotor.setPower(robot.POWER_HALF);
        sleep(1000);

        robot.armMotor.setPower(robot.POWER_STOP);
        sleep(200);

        // Get the grippers ready to grab the block
        robot.leftGrab.setPosition(robot.LEFTBLOCK_READY);
        robot.rightGrab.setPosition(robot.RIGHTBLOCK_READY);
        sleep(1000);
        */

        if (robot.colorSensorNormalized instanceof SwitchableLight) {
            ((SwitchableLight) robot.colorSensorNormalized).enableLight(false);
        }
        sleep(500);

        // Last Step - Complete!
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);

    }

}
