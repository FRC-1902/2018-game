package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
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
        double y = Utils.deadzone(OI.driver.getY(), 0.1);
        double x = Utils.deadzone(OI.driver.getX2(), 0.1);

        y=-y;

        double nonScaledY = y;

        x = Math.pow(x, 2) * Utils.sign(x);
        y = Math.pow(y, 2) * Utils.sign(y);


        //Robot.drive.tankDrive(y + x, y - x);

        boolean driverShift = OI.driver.isRightTriggerPressed();
        if (driverShift == Robot.drive.shift.get()) { //actually unequal
            Log.d("Driver shifting");
            Robot.drive.shift.set(!driverShift);
            if (driverShift) {
                stopStart = System.currentTimeMillis();
            } else {
                stopStart = 0;
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

        double left = y + x, right = y - x;

        if (x == 0/* && y != 0*/) {
            if (!Robot.drive.rotatePID.isEnabled()) {
                Robot.drive.rotatePID.enable();
                Robot.drive.rotatePID.setTarget(Robot.drive.gyro.getForPID());
                Log.d("Enabling drive forward assist");
            }
            double pidOut = Robot.drive.rotatePIDOutput.getPower() * 1;
            left += pidOut;
            right -= pidOut;

            double max = Utils.maxDouble(Math.abs(left), Math.abs(right));
            left /= max;
            right /= max;

            left *= Math.abs(y);
            right *= Math.abs(y);
        } else {
            if (Robot.drive.rotatePID.isEnabled()) {
                Robot.drive.rotatePID.disable();
                Log.d("Disabled drive forward assist");
            }
        }

        Robot.drive.tankDrive(left, right);


        //Robot.compressor.setClosedLoopControl(!OI.driver.isLeftTriggerPressed());
        //Robot.drive.shift.set(!OI.driver.isRightTriggerPressed());

    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled() || !Robot.isTeleop();
    }
}
