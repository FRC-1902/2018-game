package com.explodingbacon.powerup.core.Framework;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;

public abstract class AbstractAutoCommand extends Command {
    PIDController rotatePID;
    Motor pidOutput;

    public void passPID(PIDController rotatePID ,Motor pidOutput){
        this.rotatePID = rotatePID;
        this.pidOutput = pidOutput;
    }

    public void turnToAngle(double angle) {
        turnToAngle(angle, 5); //4
    }

    public void turnToAngle(double angle, double deadzone) {
        rotatePID.setFinishedTolerance(deadzone);
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
            double pidOut = Robot.drive.rotatePIDOutput.getPower() * 1;
            double left = speed + pidOut;
            double right = speed - pidOut;

            double max = Utils.maxDouble(Math.abs(left), Math.abs(right));
            left /= max;
            right /= max;

            left *= Math.abs(speed);
            right *= Math.abs(speed);

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
        Robot.drive.rotatePID.disable();
        Robot.drive.tankDrive(0,0);
    }

    public double inches(double inches) {
        return DriveSubsystem.inchesToClicks(inches);//inches * 0.0254;
    }

    public void resetEncs() {
        Robot.drive.leftDriveEncoder.reset();
        Robot.drive.rightDriveEncoder.reset();
    }

    public double encAverage() {
        double avg = (Robot.drive.rightDriveEncoder.getForPID() + Robot.drive.leftDriveEncoder.getForPID())/2;
        return avg;
    }

    public void sleep(long millis){
        try{
            Thread.sleep(millis);
        } catch(Exception e){
            Log.d("Sleep failed at " + e.getMessage());
        }
    }
}
