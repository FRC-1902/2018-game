package com.explodingbacon.powerup.core.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;

public class IntakeCommand extends Command {

    boolean intakePrevious = false;
    long intakeClampTime = -1;

    @Override
    public void onInit() {}

    @Override
    public void onLoop() {
        boolean intake = OI.intakeInButton.get(), outtake = OI.intakeOutButton.get() || ArmCommand.forceOuttake;

        if (!Robot.MAIN_ROBOT && !intake && intakePrevious && intakeClampTime == -1 && Robot.arm.preset == ArmSubsystem.Preset.FLOOR) {
            intakeClampTime = System.currentTimeMillis();
        }


        if (intake) {
            if (Robot.arm.preset == ArmSubsystem.Preset.FLOOR) {
                Robot.intake.setIntake(0.7, 1, true);
                if (!Robot.MAIN_ROBOT) Robot.intake.setGrasp(true);
            } else {
                Robot.intake.setIntake(1, true);
            }
        } else if (outtake) {
            double pow = Robot.arm.preset == ArmSubsystem.Preset.FLOOR ? 1 : 0.55;
            Robot.intake.setIntake(pow, false);
        } else {
            if (!Robot.MAIN_ROBOT) Robot.intake.setGrasp(false);
            double leftRate = Robot.drive.leftDriveEncoder.getRate();
            double rightRate = Robot.drive.rightDriveEncoder.getRate();

            double speed = 0;

            if (Utils.sign(leftRate) != Utils.sign(rightRate) && Math.max(Math.abs(leftRate), Math.abs(rightRate)) > 140) {
                speed = 0.3;
            }
            if (Robot.arm.armPID.isEnabled() && Math.abs(Robot.arm.armPID.getCurrentError()) > 220) {
                speed = 0.6;
            }
            if (speed > 1) speed = 1;

            if (!Robot.MAIN_ROBOT && intakeClampTime != -1) {
                if (System.currentTimeMillis() - intakeClampTime <= 500) {
                    speed = 1;
                } else {
                    intakeClampTime = -1;
                }
            }

            Robot.intake.setIntake(speed, true);
        }

        intakePrevious = intake;
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
