package com.explodingbacon.powerup.core;

public class Map {

    //SOLENOIDS

    public static final int SHIFT = Robot.MAIN_ROBOT ? 0 : 0;
    public static final int CLIMBER = Robot.MAIN_ROBOT ? -1 : -1;

    //CAN

    public static final int LEFT_DRIVE_A = Robot.MAIN_ROBOT ? 0 : 4;
    public static final int LEFT_DRIVE_B = Robot.MAIN_ROBOT ? 2 : 6;
    public static final int LEFT_DRIVE_C = Robot.MAIN_ROBOT ? 3 : 5;

    public static final int RIGHT_DRIVE_A = Robot.MAIN_ROBOT ? 1 : 7;
    public static final int RIGHT_DRIVE_B = Robot.MAIN_ROBOT ? 4 : 3;
    public static final int RIGHT_DRIVE_C = Robot.MAIN_ROBOT ? 5 : 0;

    //PWM real bot, CAN Practice

    public static final int INTAKE_TOP = Robot.MAIN_ROBOT ? 3 : 1;
    public static final int INTAKE_BOTTOM = Robot.MAIN_ROBOT ? 1 : 2;

    // PWM

    public static final int ARM_A = Robot.MAIN_ROBOT ? 0 : 0;
    public static int ARM_B = Robot.MAIN_ROBOT ? 2 : 1;


    public static final int CLIMBER_A = Robot.MAIN_ROBOT ? 2 : 2;
    public static final int CLIMBER_B = Robot.MAIN_ROBOT ? 3 : 3;


    //DIO

    public static final int DRIVE_RIGHT_ENCODER_A = Robot.MAIN_ROBOT ? 2 : 2;
    public static final int DRIVE_RIGHT_ENCODER_B = Robot.MAIN_ROBOT ? 3 : 3;

    public static final int DRIVE_LEFT_ENCODER_A = Robot.MAIN_ROBOT ? 0 : 0;
    public static final int DRIVE_LEFT_ENCODER_B = Robot.MAIN_ROBOT ? 1 : 1;

    public static final int ARM_LIMIT_FRONT = Robot.MAIN_ROBOT ? 5 : 4;
    public static final int ARM_LIMIT_BACK = Robot.MAIN_ROBOT ? 4 : 5;


    // ANALOG

    public static final int ARM_ENCODER = 0;

}
