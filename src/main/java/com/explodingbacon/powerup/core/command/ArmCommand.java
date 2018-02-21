package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;

public class ArmCommand extends Command {

    @Override
    public void onInit() { }

    double flipOffset = 0;
    boolean didFlip = false;

    @Override
    public void onLoop() {
        if (Robot.arm.frontLimit.get()) { //if front limit switch == true
            ArmSubsystem.FLOOR_FRONT = Robot.arm.getPosition();
            //ArmSubsystem.FLOOR_BACK = null;
            Robot.arm.initPresets();
            Log.d("Zeroed front");
        }

        if (Robot.arm.backLimit.get()) {
            ArmSubsystem.FLOOR_BACK = Robot.arm.getPosition()+20;
            //ArmSubsystem.FLOOR_FRONT = null;
            Robot.arm.initPresets();
            //Robot.arm.initPresets(true);
            Log.d("Zeroed back");
        }

        if ((OI.driver.isLeftTriggerPressed() || OI.manipulator.isLeftTriggerPressed()) && !Robot.arm.ohHeckMode) {
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
            if (Robot.MAIN_ROBOT ? !front : front) {
                flipOffset = -40;
            } else {
                flipOffset = 40;
            }
            pressedButton = true;

        }
        didFlip = reverse;

        if (!Robot.arm.ohHeckMode || pressedButton) {
            Robot.arm.setState(front, floor);
            Robot.arm.ohHeckMode = false;
        }

        double offset = flipOffset;

        double pow = Utils.deadzone(OI.manipulator.getY2(), 0.1);
        pow =- pow;
        if (pow != 0) {
            offset += (ArmSubsystem.MAX_OFFSET * pow) * (Robot.arm.front ? 1 : -1);
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
