package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class ArmCommand extends Command {

    @Override
    public void onInit() {}

    @Override
    public void onLoop() {
        boolean floor = Robot.arm.floor, front = Robot.arm.front;

        if (OI.armPositionOne.get()) {
            floor = false;
        } else if (OI.armPositionFour.get()) {
            floor = true;
        }

        if (OI.manipulator.rightTrigger.get()) {
            front = !front;
        }

        Robot.arm.setState(front, floor);

        //Robot.arm.toPosition(OI.armPositionOne.get(), OI.armPositionTwo.get(), OI.armPositionThree.get(), OI.armPositionFour.get());

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
