package com.explodingbacon.powerup.core.commands;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LowGearAutoCommand extends AbstractAutoCommand {

    Motor pidOutput;
    PIDController rotatePID;

    public LowGearAutoCommand() {
        pidOutput = Robot.drive.rotatePIDOutput;
        rotatePID = Robot.drive.rotatePID;
        passPID(rotatePID, pidOutput, Robot.drive.rotateInPlacePID, Robot.drive.rotateInPlacePIDOutput);
    }

    @Override
    public void onInit() {
        Robot.drive.shift(true);
        Robot.drive.gyro.rezero();
        Robot.arm.armPID.enable();
        Robot.arm.setState(true, false);
        ThreeCubeEnding ending = Robot.threeCubeEnding.getSelected();
        double cubes = SmartDashboard.getNumber("Autonomous Cubes", 3);
        try {
            double angle = 0;
            double forward = 0;

            String gameData;
            while ((gameData = DriverStation.getInstance().getGameSpecificMessage()).length() != 3) {
                sleep(5);
            }
            Log.i("GAME DATA: " + gameData);

            boolean left = gameData.charAt(0) == 'L';

            driveDistance(8, 0.5);

            angle = left ? 360-55 : 30;
            driveDistanceAtAngle(left ? 74 : 71, 1, angle);


            driveDistanceAtAngle(left ? 78 : 60, 1, 0);

            //eject cube 1
            outtake();

            if (cubes > 1) {
                //scored cube, going for #2

                final double backFromSwitch = 35.5;

                driveDistanceAtAngle(backFromSwitch, -0.6, 0);

                angle = left ? 70 : 360 - 70; //75 BOTH
                turnToAngle(angle);

                Robot.arm.setState(true, true);


                intake();

                forward = 45 + 6 + (left ? 0 : 6);
                driveDistanceAtAngle(forward, 0.5, angle);
                Robot.intake.setIntake(0.6, true);

                driveDistanceAtAngle(forward - 13, -0.6, angle);
                Robot.intake.setIntake(0.3, true);

                Robot.arm.setState(true, false);
                angle = 0;//left ? 8 : 360-8;
                turnToAngle(angle);

                stopIntake();

                driveDistanceAtAngle(backFromSwitch + 8, 0.7, angle); //TODO: slow down from 1

                //Eject cube 2
                outtake();
            }

            if (cubes > 2) {
                //backup for cube 3
                final double backFromSwitch2 = 13    + 4 + 3 + 3 + 3;

                driveDistanceAtAngle(backFromSwitch2, -0.45, 0);

                //turn for cube 3
                angle = left ? 70 : 360 - 70; //BOTH 75
                Robot.arm.setState(true, true);
                turnToAngle(angle);

                //get cube 3
                intake();

                forward = 43 + 6   + 3;
                driveDistanceAtAngle(forward, 0.6, angle); //.5
                Robot.intake.setIntake(0.4, true);

                driveDistanceAtAngle(forward - 2 - (left ? 0 : 2), -0.65, angle);

                Robot.arm.setState(true, false);

                angle = 0;//left ? 5 : 360-5;
                turnToAngle(angle);
                stopIntake();

                driveDistanceAtAngle(backFromSwitch2 + 7 + 12, 1, angle); //TODO: too much?

                //Eject cube 3
                outtake();
            }

            if (ending == ThreeCubeEnding.BACK_UP) {
                Robot.arm.setState(true, ArmSubsystem.Preset.HECK);
                driveDistanceAtAngle(24, -1, 0);
                //Robot.arm.setState(true, true);
            }

            Robot.drive.tankDrive(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoop() {}

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return true;
    }

    public enum ThreeCubeEnding {
        BACK_UP,
        CUBE_4,
        EXCHANGE
    }
}