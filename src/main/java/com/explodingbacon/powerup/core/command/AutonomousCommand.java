package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.DriverStation;

public class AutonomousCommand extends AbstractAutoCommand {

    Motor pidOutput;
    PIDController rotatePID;

    public AutonomousCommand() {
        pidOutput = Robot.drive.rotatePIDOutput;
        rotatePID = Robot.drive.rotatePID;
        passPID(rotatePID , pidOutput);
    }

    @Override
    public void onInit() {
        Robot.drive.gyro.rezero();
        Robot.arm.armPID.enable();
        Robot.arm.setState(true, false);
        Robot.drive.shift.set(true);
        String s = DriverStation.getInstance().getGameSpecificMessage();
        if (s.charAt(0) == 'L') {
            Log.d("GO LEFT");
        } else {
            Log.d("GO RIGHT");
        }
        try {
            //Thread.sleep(500);
            double angle = 0;
            boolean left = s.charAt(0) == 'L';
            //Robot.intake.setIntake(false);

            driveDistance(8, 0.5);
            sleep(200);


            angle = left ? 360-55 : 30;
            driveDistanceAtAngle(left ? 96 : 96, 1, angle); //.9


            driveDistanceAtAngle(54-(!left ? 21 : 0), 0.5, 0);


            //eject cube 1
            Robot.intake.setIntake(0.5, false);
            sleep(350);
            Robot.intake.setIntake(0, false);

            //scored cube, going to get another one from 10 stack

            final double backFromSwitch = 12;// +(!left ? 12+7 : 0);

            driveDistanceAtAngle(backFromSwitch, -0.45, 0);

            turnToAngle(left ? 90 : 360-90);


            Robot.arm.setState(true, true);


            Robot.intake.setIntake(1, true);
            driveDistance(48, 0.4);
            Robot.intake.setIntake(0.3, true);

            driveDistance(31, -0.6);

            Robot.arm.setState(true, false);
            turnToAngle(0);

            Robot.intake.setIntake(0, true);

            driveDistanceAtAngle(backFromSwitch, 0.45, 0);


            //Eject cube 2
            Robot.intake.setIntake(0.5, false);
            sleep(350);
            Robot.intake.setIntake(0, false);

            sleep(5000);

            //back up for cube 3
            driveDistance(5, -0.7);
            Robot.arm.setState(true, true);

            //turn for cube 3
            angle = left ? 67 : 360 - 67;
            turnToAngle(angle);

            //get cube 3
            Robot.intake.intake(true, false);

            driveDistance(27, 0.6);
            Thread.sleep(250);
            Robot.intake.intake(false, false);

            /*
            driveDistance(20, -0.7);

            //Has cube, backing up to exchange

            if (left) {
                Robot.drive.tankDrive(-0.5f, 0.5f);
            } else {
                Robot.drive.tankDrive(0.5f, -0.5f);
            }
            while (Math.abs((left ? 360-10 : 30)-Robot.drive.gyro.getForPID()) > 5) {
                Thread.sleep(5);
            }
            Robot.drive.tankDrive(-1,-1);
            Thread.sleep(700);
            */
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
}
