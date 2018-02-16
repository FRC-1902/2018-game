package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;

public class ArmCommand extends Command {

    @Override
    public void onInit() {}

    boolean didFlip = false;

    @Override
    public void onLoop() {
        if (false) {
            double arm = OI.manipulator.getRightTrigger() - OI.manipulator.getLeftTrigger();
            arm = Utils.deadzone(arm, 0.1);
            Robot.arm.arm.setPower(arm*.75);
        } else {

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
                double left = Math.abs(OI.manipulator.getLeftTrigger());
                offset = ArmSubsystem.MAX_OFFSET * left * (Robot.arm.front ? 1 : -1);
            }
            Robot.arm.setOffset(offset);

            //Robot.arm.toPosition(OI.armPositionOne.get(), OI.armPositionTwo.get(), OI.armPositionThree.get(), OI.armPositionFour.get());
        }

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
