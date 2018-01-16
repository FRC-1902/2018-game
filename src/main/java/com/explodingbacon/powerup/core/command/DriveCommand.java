package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class DriveCommand extends Command {

    @Override
    public void onInit() { }

    @Override
    public void onLoop() {
        double y = Utils.deadzone(OI.driver.getY(), 0.1);
        double x = Utils.deadzone(OI.driver.getX2(), 0.1);

        y=-y;

        x = Math.pow(x, 2) * Utils.sign(x);
        y = Math.pow(y, 2) * Utils.sign(y);

        Robot.drive.tankDrive(y + x, y - x);
    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled() || Robot.isAutonomous();
    }
}
