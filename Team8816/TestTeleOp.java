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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Test Mode", group = "Test")
@Disabled
public class TestTeleOp extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor armMotor = null;

    public Servo leftGrab = null;
    public Servo rightGrab = null;
    public Servo colorArm = null;

    public Servo leftTop = null;
    public Servo rightTop = null;

    // color sensor
    public ColorSensor colorSensor = null;

    // normqlized color sensor
    public NormalizedColorSensor colorSensorNormalized = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Init Hardware

        HardwareSkyBot robot = new HardwareSkyBot();

        // Define and Initialize Hardware
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        armMotor = hardwareMap.dcMotor.get("armMotor");

        leftGrab = hardwareMap.servo.get("leftGrab");
        rightGrab = hardwareMap.servo.get("rightGrab");
        colorArm = hardwareMap.servo.get("colorArm");
        leftTop = hardwareMap.servo.get("leftTop");
        rightTop = hardwareMap.servo.get("rightTop");

        colorSensor = hardwareMap.get(ColorSensor.class, "sensorColor");

        colorSensorNormalized = hardwareMap.get(NormalizedColorSensor.class, "sensorColor");

        // This will allow motors to rotate in same direction
        leftDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (gamepad1.a)
                leftTop.setPosition(0);
            else if (gamepad1.b)
                leftTop.setPosition(1);

            if (gamepad1.x)
                rightTop.setPosition(0);
            else if (gamepad1.y)
                rightTop.setPosition(1);

            // forward position or grabbed block
            if (gamepad2.y) {
                leftGrab.setPosition(1); // set position to 0 degrees
                rightGrab.setPosition(0);
                telemetry.addData("pressed", "Y");

                // side position
            } else if (gamepad2.b) {
                leftGrab.setPosition(0.75); // set position to 45 degrees
                rightGrab.setPosition(0.25);
                telemetry.addData("pressed", "B");

                // ready to grab position \ /
            } else if (gamepad2.a) {
                leftGrab.setPosition(0.9167); // set position to 15 degrees
                rightGrab.setPosition(0.0833);
                telemetry.addData("pressed", "A");
            }

            /* Debugging purposes - test the color sensor arm position
        if (gamepad1.a) {

            colorArmBest.setPosition(0.95);

        } else if (gamepad1.b) {

            colorArmBest.setPosition(0.90);

        } else if (gamepad1.x) {

            colorArmBest.setPosition(0.25);

        } else if (gamepad1.y) {

            colorArmBest.setPosition(0.5);

        }
        */

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
