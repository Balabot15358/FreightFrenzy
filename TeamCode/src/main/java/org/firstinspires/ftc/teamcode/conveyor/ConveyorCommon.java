package org.firstinspires.ftc.teamcode.conveyor;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.drivetrain.DrivetrainCommon;
import org.firstinspires.ftc.teamcode.intake.IntakeCommon;
import org.firstinspires.ftc.teamcode.spinner.SpinnerCommon;

public class ConveyorCommon {

    public ConveyorHardware robot = new ConveyorHardware();

    SpinnerCommon spinner;
    DrivetrainCommon drivetrain;
    IntakeCommon intake;

    public LinearOpMode curOpMode;

    double lastRead1 = 0;
    double lastRead2 = 0;

    double read1 = 0;
    double read2 = 0;

    public ConveyorCommon(LinearOpMode owningOpMode, DrivetrainCommon owningDrivetrain, IntakeCommon owningIntake, SpinnerCommon owningSpinner){
        curOpMode = owningOpMode;
        robot.init(curOpMode.hardwareMap);
        robot.conveyorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.conveyorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        spinner = owningSpinner;
        drivetrain = owningDrivetrain;
        intake = owningIntake;
    }

    public ConveyorCommon(LinearOpMode owningOpMode){
        curOpMode = owningOpMode;
        robot.init(curOpMode.hardwareMap);
        robot.conveyorMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.conveyorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void executeTeleop(){
        if(curOpMode.gamepad2.dpad_up){
            liftConveyor(0);
        } else if (curOpMode.gamepad2.dpad_right){
            liftConveyor(1);
        } else if (curOpMode.gamepad2.dpad_down){
            liftConveyor(2);
        } else if (curOpMode.gamepad2.dpad_left){
            liftConveyor(3);
        }

        if(curOpMode.gamepad2.back){
            pushConveyor(1);
        }

        robot.conveyorMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        printData();
    }

    public void printData(){
        curOpMode.telemetry.addData("Distance 1", read1);
        curOpMode.telemetry.addData("Distance 2", read2);
        curOpMode.telemetry.addData("Position", spawnpoint());
    }

    public void liftConveyor(int pos){
        if(pos == 0){
            robot.conveyorServo.setPosition(100f/180f);
        } else if (pos == 1){
            robot.conveyorServo.setPosition(100f/180f);
        } else if (pos == 2){
            robot.conveyorServo.setPosition(135f/180f);
        } else if (pos == 3){
            robot.conveyorServo.setPosition(180f/180f);
        }
    }

    public void pushConveyor(double speed){
        moveConveyor(2700, speed);
        moveConveyor(0, speed);
    }

    public void moveConveyor(int encoder, double speed) {
        robot.conveyorMotor.setTargetPosition(encoder);
        robot.conveyorMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.conveyorMotor.setPower(speed);
        while (robot.conveyorMotor.isBusy()) {
            checkInputs();
            curOpMode.telemetry.addData("Encoder Position", robot.conveyorMotor.getCurrentPosition());
            curOpMode.telemetry.update();
        }
        robot.conveyorMotor.setPower(0);
    }

    public void checkInputs(){
        drivetrain.executeTeleop();
        spinner.executeTeleop();
        intake.executeTeleop();
    }

    public int spawnpoint(){
        double curRead1 = robot.ds1.getDistance(DistanceUnit.INCH);
        double curRead2 = robot.ds2.getDistance(DistanceUnit.INCH);

        if (curRead1 > lastRead1){
            read1 = curRead1;
        }
        if (curRead2 > lastRead2){
            read2 = curRead2;
        }

        lastRead1 = read1;
        lastRead2 = read2;

        /*
        Sensor 1 Caught : 1
        Sensor 2 Caught : 2
        Neither Caught : 3
        Both caught : 0
         */

        if (read1 < 50 && read2 < 50){
            return 0;
        } else if (read1 < 50){
            return 1;
        } else if (read2 < 50){
            return 2;
        } else {
            return 3;
        }
    }
}