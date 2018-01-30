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
    public static Button armPositionOne;
    public static Button armPositionTwo;
    public static Button armPositionThree;
    public static Button armPositionFour;
    public static Button intakeInButton;
    public static Button intakeOutButton;

    public OI() {
        driver = new LogitechController(0);

        manipulator = new LogitechController(1);
        climberOutButton = manipulator.leftBumper;
        climberUpButton = manipulator.rightBumper;
        armPositionOne = manipulator.one;
        armPositionTwo = manipulator.two;
        armPositionThree = manipulator.three;
        armPositionFour = manipulator.four;
        intakeInButton = manipulator.rightTrigger;
        intakeOutButton = manipulator.leftTrigger;
    }
}
