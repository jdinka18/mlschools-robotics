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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

@Autonomous(name="Simple Auto Mode", group="Auto Modes")
// @Disabled
public class TimeBasedAutoMode extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareSkyBot         robot   = new HardwareSkyBot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();


    // power variables for motors (from SkyBot Class)
    public static final double POWER_FULL = 1.0;
    public static final double POWER_HALF = 0.5;
    public static final double POWER_STOP = 0.0;
    public static final double POWER_LIFT = 0.6;


    // servo positions (from SkyBot Class)
    public final static double LEFTBLOCK_READY = 0.9;
    public final static double RIGHTBLOCK_READY = 0.1;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // Step 1 - move robot forward with full speed

        robot.leftDrive.setPower(POWER_FULL);
        robot.rightDrive.setPower(POWER_FULL);
        sleep(1000);

        // Step 2 - let turn the robot!

        robot.leftDrive.setPower(POWER_HALF);
        robot.rightDrive.setPower(-POWER_HALF);
        sleep(600);

        // Step 3 - park robot by stopping the motors

        robot.leftDrive.setPower(POWER_STOP);
        robot.rightDrive.setPower(POWER_STOP);
        sleep(1000);

        // Step 3 release grippers?

        robot.leftGrab.setPosition(LEFTBLOCK_READY);
        robot.rightGrab.setPosition(RIGHTBLOCK_READY);
        sleep(1500);

        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);
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
