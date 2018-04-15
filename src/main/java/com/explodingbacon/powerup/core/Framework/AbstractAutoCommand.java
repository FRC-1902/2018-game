package com.explodingbacon.powerup.core.Framework;

import com.explodingbacon.bcnlib.actuators.Motor;
import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.framework.PIDController;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.DriveSubsystem;

public abstract class AbstractAutoCommand extends Command {
    PIDController rotatePID, rotateInPlace;
    Motor pidOutput, rotateInPlaceOutput;

    public void passPID(PIDController rotatePID, Motor pidOutput, PIDController rotateInPlace, Motor rotateInPlaceOutput){
        this.rotatePID = rotatePID;
        this.pidOutput = pidOutput;
        this.rotateInPlace = rotateInPlace;
        this.rotateInPlaceOutput = rotateInPlaceOutput;
    }

    public void turnToAngle(double angle) {
        turnToAngle(angle, 5); //4
    }

    public void turnToAngle(double angle, double deadzone) {
        if (auto()) {
            rotateInPlace.setFinishedTolerance(deadzone);
            rotateInPlace.setTarget(angle);
            rotateInPlace.enable();
            while (!rotateInPlace.isDone() && auto()) {
                Robot.drive.tankDrive(rotateInPlaceOutput.getPower(), -rotateInPlaceOutput.getPower());
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                }
            }
            rotateInPlace.disable();
        }
    }

    public void driveDistance(double inches, double speed) {
        driveDistanceAtAngle(inches, speed, Robot.drive.gyro.getForPID());
    }

    public void driveDistanceAtAngle(double inches, double speed, double angle) {
        driveAtAngle(inches, 0, speed, angle);
    }

    public void driveTimeAtAngle(long milliseconds, double speed, double angle) {
        driveAtAngle(0, milliseconds, speed, angle);
    }

    public void driveAtAngle(double inches, long milliseconds, double speed, double angle) {
        if (auto()) {
            resetEncs();
            Robot.drive.rotatePID.setTarget(angle);
            Robot.drive.rotatePID.enable();
            long startTime = System.currentTimeMillis();
            boolean keepGoing = true;
            while (keepGoing && auto()) {
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
                    if (Math.abs(Robot.drive.rightDriveEncoder.getRate()) <= 2) {
                        //Log.wtf("RIGHT DRIVE ENCODER NOT READING");
                    }
                    if (Math.abs(Robot.drive.leftDriveEncoder.getRate()) <= 2) {
                        //Log.wtf("LEFT DRIVE ENCODER NOT READING");
                    }
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
            Robot.drive.tankDrive(0, 0);
        }
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

    public void intake() {
        Robot.intake.setIntake(0.7, 1, true);
    }

    public void stopIntake() {
        Robot.intake.setIntake(0, true);
    }

    public void outtake() {
        Robot.intake.setIntake(0.4, false); //0.4
        sleep(350);
        stopIntake();
    }

    public void sleep(long millis){
        try{
            Thread.sleep(millis);
        } catch(Exception e){
            Log.d("Sleep failed at " + e.getMessage());
        }
    }

    public boolean auto() {
        return Robot.isEnabled() && Robot.isAutonomous();
    }
}
