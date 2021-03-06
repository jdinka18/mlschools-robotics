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

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a SkyBot robot.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 * <p>
 * Note: the configuration of the servos is such that:
 * As the arm servo approaches 0, the arm position moves up (away from the floor).
 * As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class HardwareSkyBot {
    /* Public OpMode members. */
    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor armMotor;

    public Servo leftGrab;
    public Servo rightGrab;
    public Servo colorArm;

    public Servo leftTop;
    public Servo rightTop;

    // color sensor
    public ColorSensor colorSensor;

    // normqlized color sensor
    public NormalizedColorSensor colorSensorNormalized;


    // various possible power values applied to the motors

    public static final double POWER_FULL = 1.0;
    public static final double POWER_HALF = 0.5;
    public static final double POWER_STOP = 0.0;
    public static final double POWER_LIFT = 0.6;


    // various values for the servos' positions (both left and right)

    public final static double LEFTGRIPPER_STOWED = 0;
    public final static double RIGHTGRIPPER_STOWED = 1;

    public final static double LEFTBLOCK_READY = 0.85;
    public final static double RIGHTBLOCK_READY = 0.15;

    public final static double LEFTTOP_READY = 0.9;
    public final static double RIGHTTOP_READY = 0.1;

    // public final static double LEFTRELICS_GRAB = 1;
//    public final static double RIGHTRELICS_GRAB = 0;

    // public double distance = sensorDistance.getDistance(DistanceUnit.CM);


    /* Local OpMode members. */
    HardwareMap hwMap = null;

    /* Constructor */
    public HardwareSkyBot() {


    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Hardware
        leftDrive = hwMap.dcMotor.get("leftDrive");
        rightDrive = hwMap.dcMotor.get("rightDrive");
        armMotor = hwMap.dcMotor.get("armMotor");

        leftGrab = hwMap.servo.get("leftGrab");
        rightGrab = hwMap.servo.get("rightGrab");
        colorArm = hwMap.servo.get("colorArm");
        leftTop = hwMap.servo.get("leftTop");
        rightTop = hwMap.servo.get("rightTop");

        colorSensor = hwMap.get(ColorSensor.class, "sensorColor");

        colorSensorNormalized = hwMap.get(NormalizedColorSensor.class, "sensorColor");

        // This will allow motors to rotate in same direction
        leftDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

    public void turnRight(double power) {

        leftDrive.setPower(power);
        rightDrive.setPower(-power);

    }

    public void turnLeft(double power) {

        leftDrive.setPower(-power);
        rightDrive.setPower(power);

    }

    public void driveStraight(double power) {

        double pwr = power;

        leftDrive.setPower(pwr);
        rightDrive.setPower(pwr);

    }

    public void stopMotors() {

        leftDrive.setPower(POWER_STOP);
        rightDrive.setPower(POWER_STOP);
    }

    public void grabBlocks() throws InterruptedException {

        leftGrab.setPosition(1); // set position to 0 degrees
        rightGrab.setPosition(0);
        leftTop.setPosition(0.6);
        rightTop.setPosition(0.4);
        Thread.sleep(1000);

    }

    public void setUpGrippers() throws InterruptedException {

        // Raise Arm with 50% power
        armMotor.setPower(POWER_HALF);
        Thread.sleep(700);

        armMotor.setPower(POWER_STOP);
        Thread.sleep(500);

        // get the grippers ready
        leftTop.setPosition(0.6);
        rightTop.setPosition(0.4);
        leftGrab.setPosition(LEFTBLOCK_READY);
        rightGrab.setPosition(RIGHTBLOCK_READY);
        Thread.sleep(1000);

    }

    public void releaseGrippers() throws InterruptedException {

        // Release the grippers
        leftGrab.setPosition(LEFTBLOCK_READY);
        rightGrab.setPosition(RIGHTBLOCK_READY);
        stopMotors();
        Thread.sleep(1000);
    }

}
