package com.explodingbacon.powerup.core.commands;

import com.explodingbacon.bcnlib.framework.Command;
import com.explodingbacon.bcnlib.framework.Log;
import com.explodingbacon.powerup.core.Robot;

public class ArmSafetyCommand extends Command {

    public static boolean ACTIVE = false;

    @Override
    public void onInit() {}

    long timeStruggling = 0;
    double encoderPrev = 0;

    @Override
    public void onLoop() {
        ACTIVE = true;
        double enc = Robot.arm.armEncoder.getForPID();
        double encSpeed = enc - encoderPrev;

        if (timeStruggling != 0)
            Log.d("struggle time: " + (System.currentTimeMillis() - timeStruggling));
        if (Robot.arm.armPID.isEnabled() && Math.abs(Robot.arm.armPID.getCurrentError()) > 240 && Math.abs(encSpeed) < 5) {
            if (timeStruggling == 0) timeStruggling = System.currentTimeMillis();
            if (System.currentTimeMillis() - timeStruggling > 1000) {
                Robot.arm.ohHeck();
                timeStruggling = 0;
                Log.e("CANCELED ARM MOVEMENT ");
            }
        } else {
            timeStruggling = 0;
        }

        encoderPrev = enc;
    }

    @Override
    public void onStop() {}

    @Override
    public boolean isFinished() {
        if (!Robot.isEnabled()) {
            ACTIVE = false;
            return true;
        }
        return false;
    }
}
