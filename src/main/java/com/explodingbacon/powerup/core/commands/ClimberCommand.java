package com.explodingbacon.powerup.core.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.powerup.core.OI;
import com.explodingbacon.powerup.core.Robot;

public class ClimberCommand extends Command {

    @Override
    public void onInit() { }

    @Override
    public void onLoop() {
        if (OI.climberWinchButton.get()) {
            Robot.climber.winch.setPower(1);
            Log.d("WINCH");
        }  else {
            Robot.climber.winch.setPower(0);
        }

        //Robot.climber.climbSol.set(!OI.climberPistonButton.get());
        //Log.d("Climb piston: " + Robot.climber.climbSol.get());
    }

    @Override
    public void onStop() {

    }

    @Override
    public boolean isFinished() {
        return !Robot.isEnabled();
    }
}
