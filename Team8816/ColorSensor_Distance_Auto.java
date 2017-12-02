package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import java.util.Locale;

/**
 * Created by SmartZalmover on 12/1/17.
 */

@Autonomous(name = "Color and Distance Senor Mode", group = "Auto Modes")

public class ColorSensor_Distance_Auto extends LinearOpMode {

    HardwareSkyBot robot = new HardwareSkyBot();

    // Variables for detecting the color

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;


    public void runOpMode() {

        // Turn on the color sensor's light
        if (robot.colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) robot.colorSensor).enableLight(true);
        }

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        robot.colorArm.setPosition(0.2);
        sleep(1000);

        while (opModeIsActive()) {
            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (robot.colorSensor.red() * SCALE_FACTOR),
                    (int) (robot.colorSensor.green() * SCALE_FACTOR),
                    (int) (robot.colorSensor.blue() * SCALE_FACTOR),
                    hsvValues);

            // send the info back to driver station using telemetry function.
            //telemetry.addData("Distance (cm)",
            //        String.format(Locale.US, "%.02f", robot.distance));
            // original: sensorDistance.getDistance(DistanceUnit.CM) ^
            telemetry.addData("Alpha", robot.colorSensor.alpha());
            telemetry.addData("Red  ", robot.colorSensor.red());
            telemetry.addData("Green", robot.colorSensor.green());
            telemetry.addData("Blue ", robot.colorSensor.blue());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.update();
        }


    }

}
