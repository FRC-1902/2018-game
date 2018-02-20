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
        /*if (Robot.arm.frontLimit.get()) { //if front limit switch == true
            ArmSubsystem.FLOOR_FRONT = Robot.arm.getPosition();
            Robot.arm.initPresets();
        }

        if (Robot.arm.backLimit.get()) {
            ArmSubsystem.FLOOR_BACK = Robot.arm.getPosition();
            Robot.arm.initPresets();
        }*/

        if (OI.driver.isLeftTriggerPressed() && !Robot.arm.ohHeckMode) {
            Robot.arm.ohHeck();
        }

        boolean pressedButton = false;
        boolean floor = Robot.arm.floor, front = Robot.arm.front;

        if (OI.armPositionOne.get()) {
            floor = false;
            pressedButton = true;
        } else if (OI.armPositionFour.get()) {
            floor = true;
            pressedButton = true;
        }

        boolean reverse = Math.abs(OI.manipulator.getRightTrigger()) > 0.1;
        if (reverse && !didFlip) {
            front = !front;
            pressedButton = true;
        }
        didFlip = reverse;

        if (!Robot.arm.ohHeckMode || pressedButton) {
            Robot.arm.setState(front, floor);
            Robot.arm.ohHeckMode = false;
        }

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
