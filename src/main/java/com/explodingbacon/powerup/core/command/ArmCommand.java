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
    boolean doSwitchThrow = false;
    public static boolean forceOuttake = false;
    long outtakeStart = 0;

    @Override
    public void onLoop() {
        /*if (Robot.arm.frontLimit.get()) { //if front limit switch == true
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
        }*/

        boolean front = Robot.arm.front;
        ArmSubsystem.Preset preset = Robot.arm.preset;


        if ((OI.driver.isLeftTriggerPressed() || OI.manipulator.isLeftTriggerPressed())) {
            preset = ArmSubsystem.Preset.HECK;
        }
        if (OI.manipulator.getDPad().isDown()) {
            preset = ArmSubsystem.Preset.HUMAN_PLAYER;
        }

        /*if (OI.driver.rightBumper.get() && Robot.arm.floor && false) {
            Robot.arm.setState(!Robot.arm.front, !Robot.arm.floor);
            doSwitchThrow = true;
            Log.d("throw");
        }

        if (doSwitchThrow && Math.abs(Robot.arm.armPID.getCurrentError()) < 1050) { //900
            Log.d("outtake");
            forceOuttake = true;
            if (outtakeStart == 0) outtakeStart = System.currentTimeMillis();
        }

        if (doSwitchThrow && outtakeStart != 0 && System.currentTimeMillis() - outtakeStart >= 1200) {
            Log.d("stop");
            outtakeStart = 0;
            forceOuttake = false;
            doSwitchThrow = false;
        }*/

        if (OI.armPositionOne.get()) {
            preset = ArmSubsystem.Preset.SWITCH;
        } else if (OI.armPositionFour.get()) {
            preset = ArmSubsystem.Preset.FLOOR;
        }

        boolean reverse = Math.abs(OI.manipulator.getRightTrigger()) > 0.1;
        if (reverse && !didFlip) {
            front = !front;
        }
        didFlip = reverse;

        Robot.arm.setState(front, preset);


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
