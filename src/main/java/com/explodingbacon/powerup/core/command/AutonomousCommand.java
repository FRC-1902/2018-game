package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;
import edu.wpi.first.wpilibj.DriverStation;

public class AutonomousCommand extends Command {

    Motor pidOutput;
    PIDController rotatePID;

    public AutonomousCommand() {
        pidOutput = Robot.drive.rotatePIDOutput;
        rotatePID = Robot.drive.rotatePID;
    }

    @Override
    public void onInit() {
        Robot.drive.gyro.rezero();
        String s = DriverStation.getInstance().getGameSpecificMessage();
        if (s.charAt(0) == 'L') {
            Log.d("GO LEFT");
        } else {
            Log.d("GO RIGHT");
        }
        try {
            double angle = 0;
            boolean left = s.charAt(0) == 'L';
            //Robot.intake.setIntake(false);

            driveDistance(12, 0.5);
            Thread.sleep(220);

            /*
            double angle = left ? 360-30 : 30;
            rotatePID.setTarget(angle);
            rotatePID.enable();
            while (!rotatePID.isDone() && Robot.isAutonomous()) {
                Robot.drive.tankDrive(pidOutput.getPower(), -pidOutput.getPower());
                Thread.sleep(5);
            }
            rotatePID.disable();
            Robot.drive.tankDrive(0,0);
            Thread.sleep(100);

            driveDistance(184, 1);*/

            driveDistanceAtAngle(184, 1, 45);

            turnToAngle(0);

            driveDistance(24, 0.7);
            Thread.sleep(200);

            //Robot.intake.outtakeCube()
            Thread.sleep(200);

            //scored cube, going to get another one from 10 stack

            driveDistance(12, -0.7);

            angle = left ? 90 : 270;
            turnToAngle(angle);

            //Robot.intake.setIntake(true)
            Robot.drive.tankDrive(0.5f,0.5f);
            Thread.sleep(1000);
            //Robot.intake.setIntake(false);

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

    public void turnToAngle(double angle) {
        rotatePID.setTarget(angle);
        rotatePID.enable();
        while (!rotatePID.isDone() && Robot.isAutonomous()) {
            Robot.drive.tankDrive(pidOutput.getPower(), -pidOutput.getPower());
            try {
                Thread.sleep(5);
            } catch (Exception e) {}
        }
        rotatePID.disable();
    }

    public void driveDistance(double inches, double speed) {
        driveDistanceAtAngle(inches, speed, Robot.drive.gyro.getForPID());
    }

    /*
    public void driveDistance(double inches, double speed) {
        resetEncs();
        Robot.drive.tankDrive(speed, speed);
        while (Math.abs(encAverage()) < inches(inches) && Robot.isAutonomous()) {
            try {
                Thread.sleep(5);
            } catch (Exception e) {}
        }
        Robot.drive.tankDrive(0,0);
    }*/

    public void driveDistanceAtAngle(double inches, double speed, double angle) {
        driveAtAngle(inches, 0, speed, angle);
    }

    public void driveTimeAtAngle(long milliseconds, double speed, double angle) {
        driveAtAngle(0, milliseconds, speed, angle);
    }

    public void driveAtAngle(double inches, long milliseconds, double speed, double angle) {
        resetEncs();
        Robot.drive.rotatePID.setTarget(angle);
        Robot.drive.rotatePID.enable();
        long startTime = System.currentTimeMillis();
        boolean keepGoing = true;
        while (keepGoing && Robot.isAutonomous()) {
            double pidOut = Robot.drive.rotatePIDOutput.getPower() * 0.75;
            double left = speed + pidOut;
            double right = speed - pidOut;

            double max = Utils.maxDouble(Math.abs(left), Math.abs(right));
            left /= max;
            right /= max;

            Robot.drive.tankDrive(left, right);

            if (inches == 0) {
                keepGoing = System.currentTimeMillis() - startTime <= milliseconds;
            } else {
                keepGoing = Math.abs(encAverage()) < inches(inches);
            }

            if (keepGoing) {
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                }
            }
        }
        Robot.drive.tankDrive(0,0);
    }

    public double inches(double inches) {
        return inches * 0.0254;
    }

    public void resetEncs() {
        Robot.drive.leftDriveEncoder.reset();
        Robot.drive.rightDriveEncoder.reset();
    }

    public double encAverage() {
        double avg = (Robot.drive.rightDriveEncoder.getForPID() + Robot.drive.leftDriveEncoder.getForPID())/2000.0;
        return (float) avg;
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
