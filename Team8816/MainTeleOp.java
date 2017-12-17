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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

//TODO: consider using 2 buttons: one to grab glyphs and one to grab the relics (which are smaller on top).


@TeleOp(name = "Main TeleOp", group = "Iterative OpModes")

public class MainTeleOp extends OpMode {
    // Declare global OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    HardwareSkyBot robot = new HardwareSkyBot();

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;
    public DcMotor armMotor = null;

    public Servo leftGrab = null;
    public Servo rightGrab = null;
    public Servo colorArm = null;

    public Servo leftTop = null;
    public Servo rightTop = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized Interative TeleOp Mode");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive = hardwareMap.dcMotor.get("leftDrive");
        rightDrive = hardwareMap.dcMotor.get("rightDrive");
        armMotor = hardwareMap.dcMotor.get("armMotor");

        leftGrab = hardwareMap.servo.get("leftGrab");
        rightGrab = hardwareMap.servo.get("rightGrab");
        colorArm = hardwareMap.servo.get("colorArm");
        leftTop = hardwareMap.servo.get("leftTop");
        rightTop = hardwareMap.servo.get("rightTop");

        /*
        left and right drive = motion of robot
        armMotor = motion of arm (lifting the grippers)
        extendingArm = motion of slider (used for dropping the fake person)
        left and right grab = grippers to get the blocks
         */

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {


        // IF YOU ARE NOT USING THE AUTO MODE

        /*

        runtime.reset();

        ElapsedTime time = new ElapsedTime();

        time.reset();

        while (time.time() < 1) {

            armMotor.setPower(0.5);

        }

        armMotor.setPower(0);

        // get the grabbers ready to grip the blocks
        leftGrab.setPosition(0.9);
        rightGrab.setPosition(0.1);

        /*
        time.reset();

        while(time.time() < 0.6) {

            armMotor.setPower(-0.5);

        }

        */


    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */

    @Override
    public void loop() {

        telemetry.addData("Status", "Interative TeleOp Mode Running");

        // Sets the power level for each driving motors
        double leftPower;
        double rightPower;

        // sets the power level for lifting the grippers
        double armPower;

        // get power value from gamepad1 (person 1) y position for driving
        leftPower = -gamepad1.left_stick_y;
        rightPower = gamepad1.right_stick_y;

        leftPower = Range.clip(leftPower, -0.6, 0.6);
        rightPower = Range.clip(rightPower, -0.6, 0.6);

        // gently raise or lower the arm (restricted to -0.5 lower | 0.5 raise)
        // get power value from gamepad2 (person 2) y position for extending arm

        armPower = -gamepad2.right_stick_y; // ensure the motor goes in the correct direction (full power possible)
        armPower = Range.clip(armPower, -0.5, 0.5);

        // Servo control: 0-0degrees, 0.5=90 degrees, 1.0=180 degrees
        // HS-485-HB servos

        // The settings for gripper arms positions (person 2)

        // forward position or grabbed block
        if (gamepad2.y) {
            leftGrab.setPosition(1); // set position to 0 degrees
            rightGrab.setPosition(0);
            telemetry.addData("pressed", "Y");

            // side position
        } else if (gamepad2.b) {
            leftGrab.setPosition(0.85); // set position to 45 degrees
            rightGrab.setPosition(0.15);
            telemetry.addData("pressed", "B");

            // ready to grab position \ /
        } else if (gamepad2.a) {
            leftGrab.setPosition(0.9167); // set position to 15 degrees
            rightGrab.setPosition(0.0833);
            telemetry.addData("pressed", "A");

        } else if (gamepad2.x) {
            leftGrab.setPosition(0);
            rightGrab.setPosition(1);
            telemetry.addData("pressed", "X");
        }

        if(gamepad1.b) {

            colorArm.setPosition(0.25); // stowed colorArm
            telemetry.addData("pressed", "B");

        }


        // top grap positions set
        if (gamepad2.left_bumper) {
            leftTop.setPosition(0.1);
            rightTop.setPosition(0.9);
            telemetry.addData("Info", "STOWED");

        }
        else if (gamepad2.dpad_up) {
            leftTop.setPosition(0.9);
            rightTop.setPosition(0.1);
            telemetry.addData("Info", "UP");

        }
        else if (gamepad2.dpad_down) {

            leftTop.setPosition(0.45);
            rightTop.setPosition(0.55);
            telemetry.addData("Info", "DOWN");

        }

        // Send calculated power to wheels
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        armMotor.setPower(armPower);
        //     extendingArm.setPower(extendingArmPower);

        // Show the elapsed game time and current status of robot's hardware.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
        telemetry.addData("Arm Motor", "(%.2f)", armMotor.getPower());
        telemetry.addData("Left Grab Servo Position", leftGrab.getPosition());
        telemetry.addData("Right Grab Servo Position", rightGrab.getPosition());
        telemetry.addData("Left Top Servo Position", leftTop.getPosition());
        telemetry.addData("Right Top Servo Position", rightTop.getPosition());
        telemetry.addData("Color Arm Position", colorArm.getPosition());
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

        leftDrive.setPower(0);
        rightDrive.setPower(0);
        armMotor.setPower(0);
        //   extendingArm.setPower(0);

        telemetry.addData("Status", "Terminated Interative TeleOp Mode");
        telemetry.update();


    }

}
