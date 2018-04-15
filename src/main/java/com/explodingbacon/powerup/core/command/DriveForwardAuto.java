package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.DriverStation;

public class DriveForwardAuto extends AbstractAutoCommand {

    Motor pidOutput;
    PIDController rotatePID;
    boolean startAtLeft, backUp;

    public DriveForwardAuto(boolean startAtLeft){
        this.startAtLeft = startAtLeft;
        pidOutput = Robot.drive.rotatePIDOutput;
        rotatePID = Robot.drive.rotatePID;
        passPID(rotatePID, pidOutput, Robot.drive.rotateInPlacePID, Robot.drive.rotateDrivingPIDOutput);
    }

    @Override
    public void onInit() {
        Robot.arm.armPID.enable();
        Robot.arm.setState(true,false);
        Robot.drive.shift(true);
        Robot.drive.gyro.rezero();
        backUp = false;

        try {
            //sleep(1000);
            String gameData;
            while ((gameData = DriverStation.getInstance().getGameSpecificMessage()).length() != 3) {
                sleep(5);
            }
            boolean left = gameData.charAt(0) == 'L';

            driveDistance((14*12)-12, 0.8);
            sleep(500);
            if (left == startAtLeft) {
                //outtake();

                if (backUp) {
                    driveDistance(30, -0.8);
                    Robot.arm.setState(true, true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoop() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
