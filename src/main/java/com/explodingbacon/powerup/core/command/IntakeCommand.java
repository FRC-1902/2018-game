package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;
import com.explodingbacon.powerup.core.subsystems.ArmSubsystem;

public class IntakeCommand extends Command {

    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {
        boolean intake = OI.intakeInButton.get(), outtake = OI.intakeOutButton.get() || ArmCommand.forceOuttake, test = OI.manipulator.a.get();

        if (intake) {
            if (Robot.arm.preset == ArmSubsystem.Preset.FLOOR) {
                Robot.intake.setIntake(0.7, 1, true); //0.7, 1
            } else {
                Robot.intake.setIntake(1, true);
            }
        } else if (outtake) {
            double pow = Robot.arm.preset == ArmSubsystem.Preset.FLOOR ? 1 : 0.6; //0.6
            Robot.intake.setIntake(pow, false);
        } else if (test) {
            Robot.intake.setIntake(1, -0.8, true);
            
        } else {
            double leftRate = Robot.drive.leftDriveEncoder.getRate();
            double rightRate = Robot.drive.rightDriveEncoder.getRate();

            double speed = 0;

            if (Utils.sign(leftRate) != Utils.sign(rightRate) && Math.max(Math.abs(leftRate), Math.abs(rightRate)) > 140) {
                speed = 0.3;
            }
            if (Robot.arm.preset == ArmSubsystem.Preset.FLOOR && Math.abs(DriveCommand.currentY) >= 0.5 && ((DriveCommand.currentY > 0 && !Robot.arm.front) || (DriveCommand.currentY < 0 && Robot.arm.front))) {
                speed = 0.3;
            }
            if (Math.abs(Robot.arm.armPID.getCurrentError()) > 220) {
                speed = 0.6;
            }
            Robot.intake.setIntake(speed, true);
        }
        //Robot.intake.intake(OI.intakeInButton.get(), OI.intakeOutButton.get());
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
