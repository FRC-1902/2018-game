package com.explodingbacon.powerup.core.command;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.utils.Utils;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class ClimberCommand extends Command {
    @Override
    public void onInit() {

    }

    @Override
    public void onLoop() {
        boolean a = OI.climberOutButton.get();
        boolean b = OI.climberUpButton.get();

        Robot.climber.climberOut(a);
        Robot.climber.climberIn(b);

    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
