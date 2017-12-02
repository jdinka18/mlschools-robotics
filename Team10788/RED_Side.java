package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import  com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;

/**
 * Created by douglasjmerritt on 12/2/17.
 */

//Auto mode lasts for 30 secs
    //THis is for RED SIDE

    @Autonomous(name="RED SIDE", group="Linear Opmode")

public class RED_Side extends LinearOpMode
{
    //Color sensor stuff
    NormalizedColorSensor colorSensor;

    View relativeLayout;

    DcMotor centerDrive;    //center_drive
    Servo autoArm;
    Color color = new Color();

    // color sensor math

    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValues[] = {0F, 0F, 0F};

    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;

    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;


    @Override public void runOpMode () throws InterruptedException
    {
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // initialize the array to display the color values
        float[] hsvValues = new float[3];
        final float values[] = hsvValues;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();


        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensor_color");

        if (colorSensor instanceof SwitchableLight)
        {
            ((SwitchableLight)colorSensor).enableLight(true);
        }

        centerDrive = hardwareMap.dcMotor.get("center_drive");
        autoArm = hardwareMap.servo.get("autoArm");

        waitForStart();

        //Get arm in the ready position
        autoArm.setPosition(0.72);
        sleep(1500);

        //read sensor to figure out which way to go
        //Start reading color sensor
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        Color.colorToHSV(colors.toColor(), hsvValues);
        int color = colors.toColor();
        float max = Math.max(Math.max(Math.max(colors.red, colors.green), colors.blue), colors.alpha);
        colors.red /= max;
        colors.green /= max;
        colors.blue /= max;
        color = colors.toColor();

        //Our sensor will always look to the left
        if (Color.blue(color) > 0x50)
        {
            //I see blue knock if off
            telemetry.addLine("Blue");
            centerDrive.setPower(-0.5);  //This makes bot go left looking down
            sleep(200);

            //Now that blue is knocked off continue to the safety zone
            autoArm.setPosition(0.0);
            sleep(1500);

            centerDrive.setPower(-0.5);  //continue left
            sleep(1000);

            //We are safe

        } else if (Color.red(color) > 0x90) {

            //I see red, knock off blue
            telemetry.addLine("Red");
            centerDrive.setPower(0.5);  //bot go right
            sleep(200);

            //get arm out of way
            autoArm.setPosition(0.0);
            sleep(1500);

            //move back to start
            centerDrive.setPower(-0.5);  //bot go left
            sleep(200);

            //Now move to safety zone
            centerDrive.setPower(-0.5);  //keep going left to safe zone
            sleep(1500);

            //We are safe

        } else {

            telemetry.addLine("Unknown");
            sleep(600);

            autoArm.setPosition(0.0);
            sleep(600);

        }

        telemetry.update();
    }
}
