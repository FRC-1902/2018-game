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
        System.out.println("Right: " + Robot.drive.rightDriveEncoder.get() + ", " + Robot.drive.rightDriveEncoder.getRate());

        System.out.println("LEft: " + Robot.drive.leftDriveEncoder.get() + ", " + Robot.drive.leftDriveEncoder.getRate());

        /*if (Robot.drive.getRate() > 390) {
            Robot.drive.shift.set(true);
        } else if(Robot.drive.getRate() < 300){
            Robot.drive.shift.set(false);
        }*/


        double y = Utils.deadzone(OI.driver.getY(), 0.1);
        double x = Utils.deadzone(OI.driver.getX2(), 0.1);

        y=-y;

        x = Math.pow(x, 2) * Utils.sign(x);
        y = Math.pow(y, 2) * Utils.sign(y);

        if(Robot.drive.getRate() < 250 && System.currentTimeMillis()- noShift > 250){
            Robot.drive.shift.set(false);
            Robot.drive.light.set(false);
            noShift = System.currentTimeMillis();
        }
        if (Robot.drive.getRate() > 450) {
            if (System.currentTimeMillis()- noShift > 250) {
                if (!Robot.drive.shift.get()) stopStart = System.currentTimeMillis();
                Robot.drive.shift.set(true);
                Robot.drive.light.set(true);
                noShift = System.currentTimeMillis();
            }
        }
        if (stopStart != 0 && System.currentTimeMillis() - stopStart <= 100) {
            if (Math.abs(y) > 0.2) {
                y = 0.2 * Utils.sign(y);
            }
            if (Math.abs(x) > 0.2) {
                x = 0.2 * Utils.sign(x);
            }
        } else {
            stopStart = 0;
        }

        /*
        if (OI.driver.rightTrigger.get()) {
            Robot.drive.shift.set(false);
        } else {
            if (!Robot.drive.shift.get()) stopStart = System.currentTimeMillis();
            Robot.drive.shift.set(true);
            if (System.currentTimeMillis() - stopStart <= 100) {
                if (Math.abs(y) > 0.2) {
                    y = 0.2 * Utils.sign(y);
                }
                if (Math.abs(x) > 0.2) {
                    x = 0.2 * Utils.sign(x);
                }
            } else {
                stopStart = 0;
            }
        }*/

        Robot.drive.tankDrive(y + x, y - x);


    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled() || Robot.isAutonomous();
    }
}
