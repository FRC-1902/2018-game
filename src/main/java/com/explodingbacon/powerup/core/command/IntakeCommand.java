package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class IntakeCommand extends Command {
    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {
        Robot.intake.intake(OI.intakeInButton.get(), OI.intakeOutButton.get());

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
