package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;

public class ArmCommand extends Command {

    @Override
    public void onInit() { }

    boolean didFlip = false;

    @Override
    public void onLoop() {
        boolean floor = Robot.arm.floor, front = Robot.arm.front;

        if (OI.armPositionOne.get()) {
            floor = false;
        } else if (OI.armPositionFour.get()) {
            floor = true;
        }

        boolean reverse = Math.abs(OI.manipulator.getRightTrigger()) > 0.1;
        if (reverse && !didFlip) {
            front = !front;
        }
        didFlip = reverse;

        Robot.arm.setState(front, floor);

        double offset = 0;
        if (OI.manipulator.isLeftTriggerPressed()) {
            double pow = Math.abs(OI.manipulator.getLeftTrigger());
            offset = (ArmSubsystem.MAX_OFFSET * pow) * (Robot.arm.front ? 1 : -1);
        }
        Robot.arm.setOffset(offset);
    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
