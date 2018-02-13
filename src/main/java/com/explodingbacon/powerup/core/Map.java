package com.explodingbacon.powerup.core;

public class Map {

    //SOLENOIDS

    public static final int SHIFT = Robot.MAIN_ROBOT ? 0 : 0;

    //CAN

    public static final int LEFT_DRIVE_A = Robot.MAIN_ROBOT ? 4 : 4;
    public static final int LEFT_DRIVE_B = Robot.MAIN_ROBOT ? 6 : 6;
    public static final int LEFT_DRIVE_C = Robot.MAIN_ROBOT ? 5 : 5;

    public static final int RIGHT_DRIVE_A = Robot.MAIN_ROBOT ? 7 : 7;
    public static final int RIGHT_DRIVE_B = Robot.MAIN_ROBOT ? 3 : 3;
    public static final int RIGHT_DRIVE_C = Robot.MAIN_ROBOT ? 0 : 0;

    public static final int INTAKE_TOP = Robot.MAIN_ROBOT ? 1 : 1;
    public static final int INTAKE_BOTTOM = Robot.MAIN_ROBOT ? 2 : 2;

    // PWM

    public static final int ARM_A = Robot.MAIN_ROBOT ? 0 : 0;
    public static int ARM_B = Robot.MAIN_ROBOT ? 1 : 1;


    public static final int CLIMBER_A = Robot.MAIN_ROBOT ? 2 : 2;
    public static final int CLIMBER_B = Robot.MAIN_ROBOT ? 3 : 3;


    public static final int DRIVE_RIGHT_ENCODER_A = 0;
    public static final int DRIVE_RIGHT_ENCODER_B = 1;

    public static final int DRIVE_LEFT_ENCODER_A = 2;
    public static final int DRIVE_LEFT_ENCODER_B = 3;


    // ANALOG

    public static final int ARM_ENCODER = 0;

}
