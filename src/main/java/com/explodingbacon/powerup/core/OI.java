package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.controllers.Button;
import com.explodingbacon.bcnlib.controllers.JoystickButton;
import com.explodingbacon.bcnlib.controllers.LogitechController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {

    public static LogitechController driver;
    public static LogitechController manipulator;
    public static Button climberOutButton;
    public static Button climberUpButton;

    public OI() {
        driver = new LogitechController(0);

        manipulator = new LogitechController(1);
        climberOutButton = manipulator.leftBumper;
        climberUpButton = manipulator.rightBumper;
    }
}
