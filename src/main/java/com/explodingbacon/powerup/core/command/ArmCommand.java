package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class ArmCommand extends Command {
    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {

        Robot.arm.toPosition(OI.armPositionOne.get(), OI.armPositionTwo.get(), OI.armPositionThree.get(), OI.armPositionFour.get());

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
