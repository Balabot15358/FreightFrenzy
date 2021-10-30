
package org.firstinspires.ftc.team15358;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Locale;

import org.firstinspires.ftc.teamcode.LiftClawCommon;
@TeleOp(name="Teleop Lift & Claw")

public class TeleOpLiftClaw extends LinearOpMode {

    @Override
    public void runOpMode() {


        LiftClawCommon liftClaw = new LiftClawCommon(this);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            liftClaw.executeTeleop();

            telemetry.update();
        }
    }

}
