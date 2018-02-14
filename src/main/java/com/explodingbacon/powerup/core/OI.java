package com.explodingbacon.powerup.core;

import com.explodingbacon.bcnlib.controllers.Button;
import com.explodingbacon.bcnlib.controllers.LogitechController;
import com.explodingbacon.bcnlib.controllers.XboxController;
import com.explodingbacon.bcnlib.framework.AbstractOI;

public class OI extends AbstractOI {

    public static XboxController driver;
    public static XboxController manipulator;
    public static Button climberOutButton;
    public static Button climberUpButton;
    public static Button armPositionOne;
    public static Button armPositionTwo;
    public static Button armPositionThree;
    public static Button armPositionFour;
    public static Button intakeInButton;
    public static Button intakeOutButton;
    public static Button intakePassiveButton;

    public OI() {
        driver = new XboxController(0);
        manipulator = new XboxController(1);

        climberOutButton = manipulator.select;
        climberUpButton = manipulator.start;

        armPositionOne = manipulator.y;
        armPositionTwo = manipulator.x;
        armPositionThree = manipulator.a;
        armPositionFour = manipulator.b;

        intakeInButton = manipulator.rightBumper;
        intakeOutButton = manipulator.leftBumper;
        intakePassiveButton = manipulator.x;
    }
}
