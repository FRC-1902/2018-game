package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.Robot;

public class QuNeoDrive extends Command {


    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {

        /*double y = Robot.forward.getPressure() - Robot.back.getPressure();
        double x = Robot.right.getPressure() - Robot.left.getPressure();

        y = Utils.deadzone(y, 0.1);
        x = Utils.deadzone(x, 0.1);

        Robot.drive.tankDrive(y + x, y - x);*/
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled() || Robot.isAutonomous();
    }
}
