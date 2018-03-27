package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.powerup.core.Framework.AbstractAutoCommand;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;
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
        //Robot.drive.shift(true);
        Robot.drive.shift(false);
        Robot.drive.gyro.rezero();
        Robot.arm.armPID.enable();
        Robot.arm.setState(true, false);
        ThreeCubeEnding ending = Robot.threeCubeEnding.getSelected();

        try {
            //sleep(300); //300
            double angle = 0;

            String gameData;
            while ((gameData = DriverStation.getInstance().getGameSpecificMessage()).length() != 3) {
                sleep(5);
            }
            Log.i("GAME DATA: " + gameData);

            boolean left = gameData.charAt(0) == 'L';
            //Robot.intake.setIntake(false);

            //driveDistance(8, 0.5);

            driveDistance(2, 0.5);
            sleep(200);


            angle = left ? 360-60 : 30;
            driveDistanceAtAngle(left ? 50 : 40, 1, angle);

            driveDistanceAtAngle(left ? 67 : 67, 1, left ? 10 : 0); //1 at 0 angle

            Robot.drive.shift(true);

            //eject cube 1
            outtake();

            sleep(500);

            //scored cube, going for #2

            final double backFromSwitch = 18.5 + 14 + 3;

            driveDistanceAtAngle(backFromSwitch, -0.6, 0);

            angle = left ? 75 : 360-75;
            turnToAngle(angle);

            Robot.arm.setState(true, true);


            intake();

            double forward = 48+3-6;
            driveDistanceAtAngle(forward, 0.5, angle);
            Robot.intake.setIntake(0.6, true);

            driveDistanceAtAngle(forward-13, -0.6, angle);
            Robot.intake.setIntake(0.3, true);

            Robot.arm.setState(true, false);
            angle = 0;//left ? 8 : 360-8;
            turnToAngle(angle);

            stopIntake();

            driveDistanceAtAngle(backFromSwitch+5, 1, angle);

            //Eject cube 2
            outtake();

            //backup for cube 3
            final double backFromSwitch2 = 17-3;

            driveDistanceAtAngle(backFromSwitch2, -0.45, 0);

            //turn for cube 3
            angle = left ? 75 : 360-75;
            Robot.arm.setState(true, true);
            turnToAngle(angle);

            //get cube 3
            intake();

            forward = 29 + 3;
            driveDistanceAtAngle(forward, 0.6, angle); //.5
            Robot.intake.setIntake(0.4, true);

            driveDistanceAtAngle(forward-2, -0.65, angle);

            Robot.arm.setState(true, false);

            angle = left ? 5 : 360-5;//left ? 15 : 360-15;
            turnToAngle(angle);
            stopIntake();

            driveDistanceAtAngle(backFromSwitch2+9, 1, angle); //TODO: too much?

            //Eject cube 3
            outtake();

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
