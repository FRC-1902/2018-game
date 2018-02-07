package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class DriveCommand extends Command {

    @Override
    public void onInit() {
        Robot.drive.setPIDEnabled(true);
    }

    long stopStart = 0;
    long noShift = 0;

    @Override
    public void onLoop() {
       // System.out.println("Right: " + Robot.drive.rightDriveEncoder.get() + ", " + Robot.drive.rightDriveEncoder.getRate());

        //System.out.println("LEft: " + Robot.drive.leftDriveEncoder.get() + ", " + Robot.drive.leftDriveEncoder.getRate());

        /*if (Robot.drive.getRate() > 390) {
            Robot.drive.shift.set(true);
        } else if(Robot.drive.getRate() < 300){
            Robot.drive.shift.set(false);
        }*/


        double y = Utils.deadzone(OI.driver.getY(), 0.1);
        double x = Utils.deadzone(OI.driver.getX2(), 0.1);

        y=-y;

        double nonScaledY = y;

        x = Math.pow(x, 2) * Utils.sign(x);
        y = Math.pow(y, 2) * Utils.sign(y);

        /*
        if(System.currentTimeMillis()- noShift > 250) {
            if (x == 0 && Math.abs(nonScaledY) > .75 && Robot.drive.getRate() < 450/4) { //collision
                    Robot.drive.shift.set(false);
                Robot.drive.light.set(false);
                noShift = System.currentTimeMillis();
            } else if (Math.abs(nonScaledY) <= .4 || Utils.sign(nonScaledY) != Utils.sign(Robot.drive.getRateNotAbs())) { //driver slowing down
                Robot.drive.shift.set(false);
                Robot.drive.light.set(false);
                noShift = System.currentTimeMillis();
            } else if (OI.driver.rightTrigger.get()) { //manual shift
                Robot.drive.shift.set(false);
                Robot.drive.light.set(false);
                noShift = System.currentTimeMillis();
            }
        }
        if (Robot.drive.getRate() > 450/2) { //accelerating
            if (x == 0 && Math.abs(nonScaledY) > .75 && System.currentTimeMillis()- noShift > 250) {
                if (!Robot.drive.shift.get()) stopStart = System.currentTimeMillis();
                Robot.drive.shift.set(true);
                Robot.drive.light.set(true);
                noShift = System.currentTimeMillis();
            }
        }
        if (stopStart != 0 && System.currentTimeMillis() - stopStart <= 200) {
            if (Math.abs(y) > 0.2) {
                y = 0.2 * Utils.sign(y);
            }
            if (Math.abs(x) > 0.2) {
                x = 0.2 * Utils.sign(x);
            }
        } else {
            stopStart = 0;
        }
        */

        Robot.drive.shift.set(!OI.driver.y.get());

        Robot.drive.tankDrive(y + x, y - x);


    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled() || Robot.isAutonomous();
    }
}
