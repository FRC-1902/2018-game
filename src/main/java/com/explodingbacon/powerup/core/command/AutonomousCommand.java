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
        passPID(rotatePID, pidOutput, Robot.drive.rotateInPlacePID, Robot.drive.rotateInPlacePIDOutput);
    }

    @Override
    public void onInit() {
        Robot.drive.shift.set(true);
        Robot.drive.gyro.rezero();
        Robot.arm.armPID.enable();
        Robot.arm.setState(true, false);
        String s = DriverStation.getInstance().getGameSpecificMessage();
        if (s.charAt(0) == 'L') {
            Log.d("GO LEFT");
        } else {
            Log.d("GO RIGHT");
        }
        try {
            Thread.sleep(300); //300
            double angle = 0;
            boolean left = s.charAt(0) == 'L';
            //Robot.intake.setIntake(false);

            driveDistance(8, 0.5);
            sleep(200);


            angle = left ? 360-55 : 30;
            driveDistanceAtAngle(left ? 96 : 96, 1, angle); //.9


            driveDistanceAtAngle(54-(!left ? 23 : 0), 0.5, 0);


            //eject cube 1
            outtake();

            //scored cube, going to get another one from 10 stack

            final double backFromSwitch = 12 + (Robot.MAIN_ROBOT ? 0 : 0); //3

            driveDistanceAtAngle(backFromSwitch, -0.45, 0);

            turnToAngle(left ? 90 : 360-90);


            Robot.arm.setState(true, true);


            intake();
            driveDistance(48, 0.4);
            Robot.intake.setIntake(0.3, true);

            driveDistance(31, -0.6); //TODO: drive at 90/360-90

            Robot.arm.setState(true, false);
            turnToAngle(left ? 5 : 360-8);

            stopIntake();

            driveDistanceAtAngle(backFromSwitch, 0.45, left ? 5 : 360-8);

            //Eject cube 2
            outtake();


            //turn for cube 3
            angle = left ? 90 : 360 - 90;
            Robot.arm.setState(true, true);
            turnToAngle(angle);

            //Robot.arm.setState(true, true);
            //sleep(300);

            //get cube 3
            intake();

            driveDistance(27, 0.5); //TODO: drive at 90/360-90
            Robot.intake.setIntake(0.4, true);

            driveDistance(23, -0.6);

            Robot.arm.setState(true, false);

            angle = left ? 5 : 360-5;
            turnToAngle(left ? 5 : angle);

            stopIntake();

            driveDistanceAtAngle(4, 0.6, angle);

            //Eject cube 3
            outtake();

            driveDistanceAtAngle(24, -1, 0);
            Robot.arm.setState(true, true);

            //Robot.drive.tankDrive(0, 0);
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
