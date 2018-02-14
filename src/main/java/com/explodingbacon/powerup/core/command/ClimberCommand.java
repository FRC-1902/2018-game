package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class ClimberCommand extends Command {

    @Override
    public void onInit() { }

    @Override
    public void onLoop() {
        Robot.climber.climb(OI.climberOutButton.get(), OI.climberUpButton.get());

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
