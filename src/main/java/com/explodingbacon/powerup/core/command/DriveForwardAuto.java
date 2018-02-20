package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveForwardAuto extends AbstractAutoCommand {

    Motor pidOutput;
    PIDController rotatePID;
    boolean startAtLeft, backUp;

    public DriveForwardAuto(){
        pidOutput = Robot.drive.rotatePIDOutput;
        rotatePID = Robot.drive.rotatePID;
        passPID(rotatePID, pidOutput, Robot.drive.rotateInPlacePID, Robot.drive.rotateDrivingPIDOutput);
    }

    @Override
    public void onInit() {
        Robot.arm.armPID.enable();
        Robot.arm.setState(true,true);
        Robot.drive.shift.set(true);
        Robot.drive.gyro.rezero();
        String s = DriverStation.getInstance().getGameSpecificMessage();
        startAtLeft = SmartDashboard.getBoolean("Start at left.", true);
        backUp = SmartDashboard.getBoolean("Back up from switch", true);

        if (s.charAt(0) == 'L') {
            Log.d("GO LEFT");
        } else {
            Log.d("GO RIGHT");
        }

        try {
            sleep(500);
            double angle = 0;
            boolean left = s.charAt(0) == 'L';


            driveDistance(130, 0.8);
            sleep(100);
            if (left == startAtLeft) {
                Robot.intake.setIntake(-1, false);
                //Robot.intake.intake(false, true);
                sleep(100);
                Robot.intake.setIntake(0, false);
                //Robot.intake.intake(false, false);

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
        return false;
    }
}
